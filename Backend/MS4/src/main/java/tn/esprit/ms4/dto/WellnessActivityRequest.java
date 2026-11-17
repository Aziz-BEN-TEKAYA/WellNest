package tn.esprit.ms4.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import tn.esprit.ms4.entity.enums.WellnessActivityType;

import java.time.LocalDateTime;

public record WellnessActivityRequest(
        @Schema(example = "BREATHING")
        @NotNull(message = "Le type d'activite est obligatoire")
        WellnessActivityType activityType,

        @Schema(example = "10")
        @NotNull(message = "La duree est obligatoire")
        @Min(value = 1, message = "La duree doit etre d'au moins 1 minute")
        @Max(value = 300, message = "La duree ne doit pas depasser 300 minutes")
        Integer durationMinutes,

        @Schema(description = "Date de l'activite (maintenant si absente)")
        @PastOrPresent(message = "La date ne peut pas etre dans le futur")
        LocalDateTime activityDate,

        @Size(max = 1000, message = "La description ne doit pas depasser 1000 caracteres")
        String description
) {
}
