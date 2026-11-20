package tn.esprit.ms2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import tn.esprit.ms2.entity.enums.AlertStatus;
import tn.esprit.ms2.entity.enums.AlertType;

import java.time.LocalDate;

@Schema(description = "Alerte de baisse de mobilite. L'ecart en % est calcule automatiquement.")
public record MobilityAlertRequest(

        @Schema(example = "1")
        @NotNull(message = "L'utilisateur est obligatoire")
        Long userId,

        @Schema(example = "2026-11-15")
        @NotNull(message = "La date de detection est obligatoire")
        @PastOrPresent(message = "La date de detection ne peut pas etre dans le futur")
        LocalDate detectedOn,

        @Schema(example = "STEP_DROP")
        @NotNull(message = "Le type d'alerte est obligatoire")
        AlertType alertType,

        @Schema(example = "5000", description = "Valeur de reference (ex. mediane des pas sur 28 jours)")
        @NotNull(message = "La valeur de reference est obligatoire")
        @Positive(message = "La valeur de reference doit etre positive")
        Double baselineValue,

        @Schema(example = "3200")
        @NotNull(message = "La valeur observee est obligatoire")
        @PositiveOrZero(message = "La valeur observee ne peut pas etre negative")
        Double observedValue,

        @Schema(example = "NEW", description = "NEW par defaut")
        AlertStatus status
) {
}
