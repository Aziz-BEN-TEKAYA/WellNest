package tn.esprit.ms1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

/** Objectif nutritionnel recu. Sans statut : ACTIF (ACTIF, ATTEINT ou ABANDONNE). */
public record ObjectifNutritionnelRequest(
        @NotBlank String utilisateurId,
        @NotNull @Positive Double objectifCalories,
        @NotNull @Positive Double objectifEau,
        @NotNull LocalDate dateDebut,
        LocalDate dateFin,
        String statut
) {
}
