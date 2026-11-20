package tn.esprit.ms1.dto;

import java.time.LocalDate;

public record SuiviHydratationResponse(
        String id,
        String utilisateurId,
        Double quantiteEau,
        LocalDate date,
        String source
) {
}
