package tn.esprit.ms6.dto;

import tn.esprit.ms6.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        Long orderId,
        BigDecimal amount,
        String currency,
        String method,
        PaymentStatus status,
        LocalDateTime paymentDate
) {
}