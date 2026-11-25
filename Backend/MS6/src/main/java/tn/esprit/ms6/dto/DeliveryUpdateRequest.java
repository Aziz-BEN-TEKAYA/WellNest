package tn.esprit.ms6.dto;

import jakarta.validation.constraints.NotNull;
import tn.esprit.ms6.enums.DeliveryStatus;

public record DeliveryUpdateRequest(
        @NotNull(message = "Le statut de livraison est obligatoire")
        DeliveryStatus status
) {
}