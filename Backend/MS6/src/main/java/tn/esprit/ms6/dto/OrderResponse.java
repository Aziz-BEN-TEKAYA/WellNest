package tn.esprit.ms6.dto;

import tn.esprit.ms6.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String buyerUserId,
        String beneficiaryUserId,
        LocalDateTime orderDate,
        BigDecimal totalAmount,
        OrderStatus status,
        List<OrderItemResponse> items
) {
}