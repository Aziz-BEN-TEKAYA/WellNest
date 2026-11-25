package tn.esprit.ms6.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms6.dto.DeliveryRequest;
import tn.esprit.ms6.dto.DeliveryResponse;
import tn.esprit.ms6.dto.DeliveryUpdateRequest;
import tn.esprit.ms6.dto.TrackingResponse;
import tn.esprit.ms6.entity.Delivery;
import tn.esprit.ms6.entity.Order;
import tn.esprit.ms6.enums.DeliveryStatus;
import tn.esprit.ms6.enums.OrderStatus;
import tn.esprit.ms6.exception.ConflictException;
import tn.esprit.ms6.exception.ResourceNotFoundException;
import tn.esprit.ms6.mapper.DeliveryMapper;
import tn.esprit.ms6.repository.DeliveryRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private static final int ESTIMATED_DAYS = 5;

    private final DeliveryRepository deliveryRepository;
    private final OrderService orderService;
    private final DeliveryMapper deliveryMapper;

    /** La livraison n'existe qu'après confirmation : la commande doit être PAID. */
    @Transactional
    public DeliveryResponse create(Long orderId, DeliveryRequest request) {
        Order order = orderService.getEntity(orderId);
        if (deliveryRepository.findByOrderId(orderId).isPresent()) {
            throw new ConflictException("Cette commande a déjà une livraison");
        }
        if (order.getStatus() != OrderStatus.PAID) {
            throw new ConflictException("La livraison ne peut être créée que pour une commande payée (PAID) : "
                    + "statut actuel = " + order.getStatus());
        }

        LocalDateTime now = LocalDateTime.now();
        Delivery delivery = Delivery.builder()
                .address(request.address().trim())
                .status(DeliveryStatus.PREPARING)
                .trackingNumber(generateTrackingNumber())
                .preparationDate(now)
                .estimatedDeliveryDate(now.plusDays(ESTIMATED_DAYS))
                .order(order)
                .build();
        return deliveryMapper.toResponse(deliveryRepository.save(delivery));
    }

    @Transactional(readOnly = true)
    public DeliveryResponse findByOrderId(Long orderId) {
        return deliveryMapper.toResponse(getByOrderId(orderId));
    }

    /**
     * Avance la livraison d'UNE étape : PREPARING vers IN_TRANSIT, puis IN_TRANSIT vers DELIVERED.
     * La commande suit : SHIPPED puis DELIVERED. Si la commande refuse la transition,
     * toute la transaction est annulée : livraison et commande restent cohérentes.
     */
    @Transactional
    public DeliveryResponse update(Long orderId, DeliveryUpdateRequest request) {
        Delivery delivery = getByOrderId(orderId);
        DeliveryStatus current = delivery.getStatus();
        DeliveryStatus target = request.status();
        LocalDateTime now = LocalDateTime.now();

        if (current == DeliveryStatus.PREPARING && target == DeliveryStatus.IN_TRANSIT) {
            orderService.markShipped(orderId);
            delivery.setStatus(DeliveryStatus.IN_TRANSIT);
            delivery.setShippingDate(now);
        } else if (current == DeliveryStatus.IN_TRANSIT && target == DeliveryStatus.DELIVERED) {
            orderService.markDelivered(orderId);
            delivery.setStatus(DeliveryStatus.DELIVERED);
            delivery.setDeliveryDate(now);
        } else {
            throw new ConflictException("Transition de livraison interdite : " + current + " vers " + target);
        }
        return deliveryMapper.toResponse(delivery);
    }

    /** Suivi public par numéro de suivi (sans adresse). */
    @Transactional(readOnly = true)
    public TrackingResponse track(String trackingNumber) {
        Delivery delivery = deliveryRepository.findByTrackingNumber(trackingNumber.trim().toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Numéro de suivi introuvable : " + trackingNumber.trim()));
        return deliveryMapper.toTracking(delivery);
    }

    // ----- méthodes privées -----

    private Delivery getByOrderId(Long orderId) {
        orderService.getEntity(orderId); // 404 si la commande n'existe pas
        return deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Aucune livraison pour la commande " + orderId));
    }

    /** Format « WN-XXXXXXXX » ; on régénère tant que le numéro existe déjà. */
    private String generateTrackingNumber() {
        String candidate;
        do {
            candidate = "WN-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        } while (deliveryRepository.findByTrackingNumber(candidate).isPresent());
        return candidate;
    }
}