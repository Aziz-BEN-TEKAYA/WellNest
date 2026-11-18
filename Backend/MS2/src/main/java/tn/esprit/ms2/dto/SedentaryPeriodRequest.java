package tn.esprit.ms2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

@Schema(description = "Periode prolongee sans activite. Indiquer l'heure de fin ou la duree.")
public record SedentaryPeriodRequest(

        @Schema(example = "1")
        @NotNull(message = "L'utilisateur est obligatoire")
        Long userId,

        @Schema(example = "2026-11-15T13:00:00")
        @NotNull(message = "L'heure de debut est obligatoire")
        @PastOrPresent(message = "L'heure de debut ne peut pas etre dans le futur")
        LocalDateTime startTime,

        @Schema(example = "2026-11-15T15:30:00")
        LocalDateTime endTime,

        @Schema(example = "150", description = "Ignoree si l'heure de fin est fournie")
        @Positive(message = "La duree doit etre positive")
        Integer durationMinutes
) {
}
