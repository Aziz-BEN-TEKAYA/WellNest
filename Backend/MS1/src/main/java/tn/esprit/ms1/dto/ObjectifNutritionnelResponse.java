package tn.esprit.ms1.dto;

import java.time.LocalDate;

public record ObjectifNutritionnelResponse(
        String id,
        String utilisateurId,
        Double objectifCalories,
        Double objectifEau,
        LocalDate dateDebut,
        LocalDate dateFin,
        String statut
) {
}
