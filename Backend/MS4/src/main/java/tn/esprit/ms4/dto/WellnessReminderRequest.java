package tn.esprit.ms4.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import tn.esprit.ms4.entity.enums.ReminderType;

import java.time.LocalTime;

public record WellnessReminderRequest(
        @Schema(example = "SLEEP_LOG")
        @NotNull(message = "Le type de rappel est obligatoire")
        ReminderType reminderType,

        @Schema(example = "08:00:00", type = "string")
        @NotNull(message = "L'heure du rappel est obligatoire")
        LocalTime reminderTime,

        @Schema(example = "true", description = "Actif par defaut")
        Boolean enabled
) {
}
