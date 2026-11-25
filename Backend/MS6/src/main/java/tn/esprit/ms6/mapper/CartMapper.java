package tn.esprit.ms6.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.ms6.dto.CartItemResponse;
import tn.esprit.ms6.dto.CartRequest;
import tn.esprit.ms6.dto.CartResponse;
import tn.esprit.ms6.entity.Cart;
import tn.esprit.ms6.entity.CartItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class CartMapper {

    public Cart toEntity(CartRequest request) {
        return Cart.builder()
                .userId(request.userId().trim())
                .build();
    }

    public CartItemResponse toItemResponse(CartItem item) {
        BigDecimal lineTotal = item.getUnitPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()))
                .setScale(3, RoundingMode.HALF_UP);
        return new CartItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getUnitPrice(),
                lineTotal
        );
    }

    public CartResponse toResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream()
                .map(this::toItemResponse)
                .toList();
        BigDecimal total = items.stream()
                .map(CartItemResponse::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(3, RoundingMode.HALF_UP);
        return new CartResponse(cart.getId(), cart.getUserId(), cart.getCreatedAt(), items, total);
    }
}