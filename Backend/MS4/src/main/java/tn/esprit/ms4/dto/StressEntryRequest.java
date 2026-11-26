package tn.esprit.ms4.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record StressEntryRequest(
        @Schema(example = "2", description = "De 1 (calme) a 5 (tres stresse)")
        @NotNull(message = "Le niveau de stress est obligatoire")
        @Min(value = 1, message = "Le niveau de stress doit etre compris entre 1 et 5")
        @Max(value = 5, message = "Le niveau de stress doit etre compris entre 1 et 5")
        Integer stressLevel,

        @Schema(example = "Rendez-vous medical")
        @Size(max = 255, message = "La cause ne doit pas depasser 255 caracteres")
        String cause,

        @Size(max = 1000, message = "Les notes ne doivent pas depasser 1000 caracteres")
        String notes,

        @Schema(description = "Moment ou le stress a ete ressenti (maintenant si absent)")
        @PastOrPresent(message = "La date ne peut pas etre dans le futur")
        LocalDateTime recordedAt
) {
}
