package tn.esprit.ms2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import tn.esprit.ms2.entity.enums.DataSource;

import java.time.LocalDateTime;

@Schema(description = "Seance d'activite. Indiquer l'heure de fin ou la duree.")
public record ActivitySessionRequest(

        @Schema(example = "1")
        @NotNull(message = "L'utilisateur est obligatoire")
        Long userId,

        @Schema(example = "2")
        @NotNull(message = "Le type d'activite est obligatoire")
        Long activityTypeId,

        @Schema(example = "2026-11-15T09:30:00")
        @NotNull(message = "L'heure de debut est obligatoire")
        @PastOrPresent(message = "L'heure de debut ne peut pas etre dans le futur")
        LocalDateTime startTime,

        @Schema(example = "2026-11-15T10:10:00")
        LocalDateTime endTime,

        @Schema(example = "40", description = "Ignoree si l'heure de fin est fournie")
        @Positive(message = "La duree doit etre positive")
        Integer durationMinutes,

        @Schema(example = "2500")
        @PositiveOrZero(message = "La distance ne peut pas etre negative")
        Double distanceMeters,

        @Schema(example = "4", description = "Effort ressenti de 1 a 10")
        @Min(value = 1, message = "L'effort ressenti va de 1 a 10")
        @Max(value = 10, message = "L'effort ressenti va de 1 a 10")
        Integer perceivedEffort,

        @Size(max = 1000, message = "Les notes ne doivent pas depasser 1000 caracteres")
        String notes,

        @Schema(example = "MANUAL", description = "MANUAL par defaut")
        DataSource source
) {
}
