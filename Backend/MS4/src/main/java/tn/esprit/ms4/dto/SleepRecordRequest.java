package tn.esprit.ms4.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

/** Saisie manuelle d'une nuit. La duree n'est pas saisie : elle est calculee par le backend. */
public record SleepRecordRequest(
        @Schema(example = "2026-11-12T22:30:00")
        @NotNull(message = "L'heure du coucher est obligatoire")
        LocalDateTime bedtime,

        @Schema(example = "2026-11-13T06:30:00")
        @NotNull(message = "L'heure du reveil est obligatoire")
        LocalDateTime wakeupTime,

        @Schema(example = "4", description = "Qualite ressentie de 1 a 5")
        @NotNull(message = "La qualite du sommeil est obligatoire")
        @Min(value = 1, message = "La qualite doit etre comprise entre 1 et 5")
        @Max(value = 5, message = "La qualite doit etre comprise entre 1 et 5")
        Integer sleepQuality,

        @Schema(example = "1", description = "Nombre de reveils nocturnes (0 par defaut)")
        @Min(value = 0, message = "Le nombre de reveils ne peut pas etre negatif")
        @Max(value = 20, message = "Le nombre de reveils semble trop eleve")
        Integer nightWakings,

        @Schema(example = "Reveil en forme")
        @Size(max = 1000, message = "Les notes ne doivent pas depasser 1000 caracteres")
        String notes
) {
}
