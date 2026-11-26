package tn.esprit.ms6.dto;

import jakarta.validation.constraints.NotNull;

public record StockAdjustmentRequest(
        @NotNull(message = "La variation de stock est obligatoire")
        Integer delta
) {
}