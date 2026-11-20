package tn.esprit.ms2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import tn.esprit.ms2.entity.enums.DataSource;

import java.time.LocalDate;

@Schema(description = "Resume de l'activite d'une journee (un seul par utilisateur et par jour)")
public record DailySummaryRequest(

        @Schema(example = "1")
        @NotNull(message = "L'utilisateur est obligatoire")
        Long userId,

        @Schema(example = "2026-11-15")
        @NotNull(message = "La date est obligatoire")
        @PastOrPresent(message = "La date ne peut pas etre dans le futur")
        LocalDate date,

        @Schema(example = "4200")
        @NotNull(message = "Le nombre de pas est obligatoire")
        @PositiveOrZero(message = "Le nombre de pas ne peut pas etre negatif")
        Integer steps,

        @Schema(example = "3100")
        @PositiveOrZero(message = "La distance ne peut pas etre negative")
        Double distanceMeters,

        @Schema(example = "35")
        @PositiveOrZero(message = "Les minutes actives ne peuvent pas etre negatives")
        @Max(value = 1440, message = "Une journee compte au plus 1440 minutes")
        Integer activeMinutes,

        @Schema(example = "540")
        @PositiveOrZero(message = "Les minutes sedentaires ne peuvent pas etre negatives")
        @Max(value = 1440, message = "Une journee compte au plus 1440 minutes")
        Integer sedentaryMinutes,

        @Schema(example = "MANUAL", description = "MANUAL par defaut")
        DataSource source
) {
}
