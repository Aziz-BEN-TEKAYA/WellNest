package tn.esprit.ms6.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CartResponse(
        Long id,
        String userId,
        LocalDateTime createdAt,
        List<CartItemResponse> items,
        BigDecimal totalAmount
) {
}