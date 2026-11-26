package tn.esprit.ms6.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms6.dto.OrderItemResponse;
import tn.esprit.ms6.dto.OrderRequest;
import tn.esprit.ms6.dto.OrderResponse;
import tn.esprit.ms6.entity.Cart;
import tn.esprit.ms6.entity.Order;
import tn.esprit.ms6.entity.OrderItem;
import tn.esprit.ms6.enums.OrderStatus;
import tn.esprit.ms6.exception.BadRequestException;
import tn.esprit.ms6.exception.ConflictException;
import tn.esprit.ms6.exception.ResourceNotFoundException;
import tn.esprit.ms6.mapper.OrderMapper;
import tn.esprit.ms6.repository.OrderItemRepository;
import tn.esprit.ms6.repository.OrderRepository;
import tn.esprit.ms6.repository.ProductRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderService {

    /** Le « plan de circulation » des statuts : de chaque statut, où peut-on aller ? */
    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_TRANSITIONS = Map.of(
            OrderStatus.PENDING, Set.of(OrderStatus.PAID, OrderStatus.PAYMENT_FAILED, OrderStatus.CANCELLED),
            OrderStatus.PAID, Set.of(OrderStatus.SHIPPED, OrderStatus.CANCELLED),
            OrderStatus.SHIPPED, Set.of(OrderStatus.DELIVERED),
            OrderStatus.PAYMENT_FAILED, Set.of(),
            OrderStatus.DELIVERED, Set.of(),
            OrderStatus.CANCELLED, Set.of()
    );

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final OrderMapper orderMapper;

    /** Une « photo » d'une ligne : des valeurs simples, qui restent valables même si la session est vidée. */
    private record Line(Long productId, String productName, BigDecimal unitPrice, int quantity) {
    }

    /**
     * Création de la commande depuis le panier.
     *
     * Piège : reserveStock vide le cache de la session (clearAutomatically = true).
     * Après lui, les objets chargés avant (panier, produits) ne sont plus utilisables.
     * C'est comme prendre une photo de ta liste de courses avant d'entrer dans un magasin :
     * on travaille ensuite avec la photo (Line), pas avec la liste d'origine.
     */
    @Transactional
    public OrderResponse create(OrderRequest request) {
        Cart cart = cartService.getEntity(request.cartId());
        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Le panier est vide : impossible de passer commande");
        }

        // 1. Photo du panier AVANT de toucher au stock. Le prix est celui du produit AUJOURD'HUI.
        String buyerUserId = cart.getUserId();
        List<Line> lines = cart.getItems().stream()
                .map(ci -> new Line(
                        ci.getProduct().getId(),
                        ci.getProduct().getName(),
                        ci.getProduct().getPrice(),
                        ci.getQuantity()))
                .toList();

        // 2. Réservation atomique, ligne par ligne. Si UNE ligne échoue, l'exception annule toute la
        //    transaction (rollback) : les réservations précédentes disparaissent. Tout ou rien.
        for (Line line : lines) {
            if (productRepository.reserveStock(line.productId(), line.quantity()) == 0) {
                throw new ConflictException("Stock insuffisant pour « " + line.productName()
                        + " » : commande refusée, aucun stock n'a été retiré");
            }
        }

        // 3. Construction de la commande avec les prix figés.
        Order order = Order.builder()
                .buyerUserId(buyerUserId)
                .beneficiaryUserId(cleanOrNull(request.beneficiaryUserId()))
                .status(OrderStatus.PENDING)
                .build();
        BigDecimal total = BigDecimal.ZERO;
        for (Line line : lines) {
            order.addItem(OrderItem.builder()
                    .quantity(line.quantity())
                    .unitPrice(line.unitPrice().setScale(3, RoundingMode.HALF_UP))
                    .product(productRepository.getReferenceById(line.productId()))
                    .build());
            total = total.add(line.unitPrice().multiply(BigDecimal.valueOf(line.quantity())));
        }
        order.setTotalAmount(total.setScale(3, RoundingMode.HALF_UP));
        orderRepository.save(order);

        // 4. Le panier est vidé (on le recharge : l'ancien objet a été détaché par reserveStock).
        cartService.getEntity(request.cartId()).getItems().clear();

        return orderMapper.toResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll(String buyerUserId, String beneficiaryUserId) {
        String buyer = cleanOrNull(buyerUserId);
        String beneficiary = cleanOrNull(beneficiaryUserId);

        List<Order> orders;
        if (buyer != null) {
            orders = orderRepository.findByBuyerUserId(buyer);
        } else if (beneficiary != null) {
            orders = orderRepository.findByBeneficiaryUserId(beneficiary);
        } else {
            orders = orderRepository.findAll();
        }
        if (buyer != null && beneficiary != null) {
            orders = orders.stream()
                    .filter(o -> beneficiary.equals(o.getBeneficiaryUserId()))
                    .toList();
        }
        return orders.stream()
                .sorted(Comparator.comparing(Order::getOrderDate).reversed())
                .map(orderMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse findById(Long id) {
        return orderMapper.toResponse(getEntity(id));
    }

    @Transactional(readOnly = true)
    public List<OrderItemResponse> findItems(Long orderId) {
        getEntity(orderId); // 404 si la commande n'existe pas
        return orderItemRepository.findByOrderId(orderId).stream()
                .map(orderMapper::toItemResponse)
                .toList();
    }

    /**
     * Annulation : la commande passe à CANCELLED et le stock est rendu. Elle reste en base (historique).
     * Si une livraison existe, elle est forcément encore en préparation (sinon la commande serait
     * SHIPPED et non annulable) : on la supprime.
     */
    @Transactional
    public OrderResponse cancel(Long id) {
        Order order = getEntity(id);
        if (!ALLOWED_TRANSITIONS.get(order.getStatus()).contains(OrderStatus.CANCELLED)) {
            throw new ConflictException("La commande ne peut plus être annulée (statut : " + order.getStatus() + ")");
        }

        // Photo des lignes AVANT de rendre le stock (releaseStock vide la session).
        List<Line> lines = linesOf(order);

        if (order.getDelivery() != null) {
            order.setDelivery(null); // orphanRemoval : la livraison en préparation est supprimée
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.saveAndFlush(order); // enregistré avant que la session soit vidée

        restoreStock(lines);

        // On relit la commande : l'ancien objet est détaché.
        return orderMapper.toResponse(getEntity(id));
    }

    // ----- Changements de statut pilotés par le paiement et la livraison -----

    /** Appelée par PaymentService quand le paiement réussit. */
    @Transactional
    public void markPaid(Long orderId) {
        changeStatus(orderId, OrderStatus.PAID);
    }

    /** Appelée par PaymentService quand le paiement échoue : statut final et stock rendu. */
    @Transactional
    public void markPaymentFailed(Long orderId) {
        Order order = getEntity(orderId);
        checkTransition(order, OrderStatus.PAYMENT_FAILED);
        List<Line> lines = linesOf(order);
        order.setStatus(OrderStatus.PAYMENT_FAILED);
        orderRepository.saveAndFlush(order);
        restoreStock(lines);
    }

    /** Appelée par DeliveryService quand la livraison passe en transit. */
    @Transactional
    public void markShipped(Long orderId) {
        changeStatus(orderId, OrderStatus.SHIPPED);
    }

    /** Appelée par DeliveryService quand la livraison est terminée. */
    @Transactional
    public void markDelivered(Long orderId) {
        changeStatus(orderId, OrderStatus.DELIVERED);
    }

    /** Réutilisée par PaymentService et DeliveryService. */
    @Transactional(readOnly = true)
    public Order getEntity(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande introuvable (id = " + id + ")"));
    }

    // ----- méthodes privées -----

    private void changeStatus(Long orderId, OrderStatus target) {
        Order order = getEntity(orderId);
        checkTransition(order, target);
        order.setStatus(target);
    }

    private void checkTransition(Order order, OrderStatus target) {
        if (!ALLOWED_TRANSITIONS.get(order.getStatus()).contains(target)) {
            throw new ConflictException("Transition interdite : " + order.getStatus() + " vers " + target);
        }
    }

    private List<Line> linesOf(Order order) {
        return order.getItems().stream()
                .map(item -> new Line(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getUnitPrice(),
                        item.getQuantity()))
                .toList();
    }

    private void restoreStock(List<Line> lines) {
        for (Line line : lines) {
            productRepository.releaseStock(line.productId(), line.quantity());
        }
    }

    private String cleanOrNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}