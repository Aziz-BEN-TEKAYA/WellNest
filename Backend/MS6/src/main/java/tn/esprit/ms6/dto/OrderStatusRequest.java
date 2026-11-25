package tn.esprit.ms6.dto;

import jakarta.validation.constraints.NotNull;
import tn.esprit.ms6.enums.OrderStatus;

public record OrderStatusRequest(
        @NotNull(message = "Le statut est obligatoire")
        OrderStatus status
) {
}