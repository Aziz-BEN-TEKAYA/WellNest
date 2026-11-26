package tn.esprit.ms4.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import tn.esprit.ms4.entity.enums.MoodLevel;

import java.time.LocalDateTime;

public record MoodEntryRequest(
        @Schema(example = "GOOD")
        @NotNull(message = "L'humeur est obligatoire")
        MoodLevel moodLevel,

        @Schema(example = "Belle promenade ce matin")
        @Size(max = 1000, message = "La description ne doit pas depasser 1000 caracteres")
        String description,

        @Schema(description = "Moment de la saisie (maintenant si absent)")
        @PastOrPresent(message = "La date ne peut pas etre dans le futur")
        LocalDateTime recordedAt
) {
}
