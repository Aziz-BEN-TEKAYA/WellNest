package tn.esprit.ms6.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemUpdateRequest(
        @NotNull(message = "La quantité est obligatoire")
        @Min(value = 1, message = "La quantité doit être d'au moins 1")
        Integer quantity
) {
}