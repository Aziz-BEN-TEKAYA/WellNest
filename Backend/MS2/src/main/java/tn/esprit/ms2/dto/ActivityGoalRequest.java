package tn.esprit.ms2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import tn.esprit.ms2.entity.enums.GoalType;

import java.time.LocalDate;

@Schema(description = "Objectif d'activite d'un utilisateur")
public record ActivityGoalRequest(

        @Schema(example = "1")
        @NotNull(message = "L'utilisateur est obligatoire")
        Long userId,

        @Schema(example = "DAILY_STEPS")
        @NotNull(message = "Le type d'objectif est obligatoire")
        GoalType goalType,

        @Schema(example = "5000", description = "Pas, minutes ou nombre de seances selon le type")
        @NotNull(message = "La valeur cible est obligatoire")
        @Positive(message = "La valeur cible doit etre positive")
        Integer targetValue,

        @Schema(example = "2026-11-15")
        @NotNull(message = "La date de debut est obligatoire")
        LocalDate startDate,

        @Schema(example = "2026-12-15", description = "Facultative : sans date de fin, l'objectif est permanent")
        LocalDate endDate,

        @Schema(example = "true", description = "true par defaut")
        Boolean active
) {
}
