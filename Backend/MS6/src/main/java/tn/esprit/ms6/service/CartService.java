package tn.esprit.ms6.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms6.dto.CartItemRequest;
import tn.esprit.ms6.dto.CartItemResponse;
import tn.esprit.ms6.dto.CartItemUpdateRequest;
import tn.esprit.ms6.dto.CartRequest;
import tn.esprit.ms6.dto.CartResponse;
import tn.esprit.ms6.entity.Cart;
import tn.esprit.ms6.entity.CartItem;
import tn.esprit.ms6.entity.Product;
import tn.esprit.ms6.exception.ConflictException;
import tn.esprit.ms6.exception.ResourceNotFoundException;
import tn.esprit.ms6.mapper.CartMapper;
import tn.esprit.ms6.repository.CartItemRepository;
import tn.esprit.ms6.repository.CartRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final CartMapper cartMapper;

    // ----- Paniers -----

    @Transactional
    public CartResponse create(CartRequest request) {
        String userId = request.userId().trim();
        if (cartRepository.existsByUserId(userId)) {
            throw new ConflictException("L'utilisateur « " + userId + " » a déjà un panier");
        }
        Cart saved = cartRepository.save(cartMapper.toEntity(request));
        return cartMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public CartResponse findByUserId(String userId) {
        Cart cart = cartRepository.findByUserId(userId.trim())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Aucun panier pour l'utilisateur « " + userId.trim() + " »"));
        return cartMapper.toResponse(cart);
    }

    @Transactional(readOnly = true)
    public CartResponse findById(Long id) {
        return cartMapper.toResponse(getEntity(id));
    }

    /** Supprime le panier et toutes ses lignes (cascade). */
    @Transactional
    public void delete(Long id) {
        cartRepository.delete(getEntity(id));
    }

    // ----- Lignes du panier -----

    /** Ajoute un produit ; s'il est déjà dans le panier, les quantités s'additionnent. */
    @Transactional
    public CartItemResponse addItem(Long cartId, CartItemRequest request) {
        Cart cart = getEntity(cartId);
        Product product = productService.getEntity(request.productId());

        CartItem item = cartItemRepository.findByCartIdAndProductId(cartId, product.getId()).orElse(null);
        int newQuantity = request.quantity() + (item == null ? 0 : item.getQuantity());
        checkStock(product, newQuantity);

        if (item == null) {
            item = CartItem.builder()
                    .quantity(newQuantity)
                    .unitPrice(product.getPrice())
                    .cart(cart)
                    .product(product)
                    .build();
            cartItemRepository.save(item);
            cart.getItems().add(item);
        } else {
            item.setQuantity(newQuantity);
        }
        return cartMapper.toItemResponse(item);
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> findItems(Long cartId) {
        getEntity(cartId); // 404 si le panier n'existe pas
        return cartItemRepository.findByCartId(cartId).stream()
                .map(cartMapper::toItemResponse)
                .toList();
    }

    @Transactional
    public CartItemResponse updateItem(Long cartId, Long itemId, CartItemUpdateRequest request) {
        getEntity(cartId);
        CartItem item = getItem(cartId, itemId);
        checkStock(item.getProduct(), request.quantity());
        item.setQuantity(request.quantity());
        return cartMapper.toItemResponse(item);
    }

    @Transactional
    public void deleteItem(Long cartId, Long itemId) {
        Cart cart = getEntity(cartId);
        CartItem item = getItem(cartId, itemId);
        cart.getItems().remove(item); // orphanRemoval : la ligne est supprimée en base
    }

    /** Réutilisée plus tard par OrderService pour retrouver le panier. */
    @Transactional(readOnly = true)
    public Cart getEntity(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Panier introuvable (id = " + id + ")"));
    }

    // ----- méthodes privées -----

    /** Une ligne n'est valable que si elle appartient bien à ce panier. */
    private CartItem getItem(Long cartId, Long itemId) {
        return cartItemRepository.findById(itemId)
                .filter(item -> item.getCart().getId().equals(cartId))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ligne introuvable (id = " + itemId + ") dans le panier " + cartId));
    }

    private void checkStock(Product product, int requestedQuantity) {
        if (product.getStock() <= 0) {
            throw new ConflictException("Le produit « " + product.getName() + " » est en rupture de stock");
        }
        if (requestedQuantity > product.getStock()) {
            throw new ConflictException("Stock insuffisant pour « " + product.getName()
                    + " » : disponible = " + product.getStock() + ", demandé = " + requestedQuantity);
        }
    }
}