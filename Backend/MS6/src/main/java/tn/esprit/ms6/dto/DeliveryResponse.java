package tn.esprit.ms6.dto;

import tn.esprit.ms6.enums.DeliveryStatus;

import java.time.LocalDateTime;

public record DeliveryResponse(
        Long id,
        Long orderId,
        String address,
        DeliveryStatus status,
        String trackingNumber,
        LocalDateTime estimatedDeliveryDate,
        LocalDateTime preparationDate,
        LocalDateTime shippingDate,
        LocalDateTime deliveryDate
) {
}