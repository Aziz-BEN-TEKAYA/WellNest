package tn.esprit.ms6.dto;

import tn.esprit.ms6.enums.DeliveryStatus;

import java.time.LocalDateTime;

public record TrackingResponse(
        String trackingNumber,
        DeliveryStatus status,
        LocalDateTime estimatedDeliveryDate,
        LocalDateTime preparationDate,
        LocalDateTime shippingDate,
        LocalDateTime deliveryDate
) {
}