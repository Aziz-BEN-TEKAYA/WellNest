package tn.esprit.ms1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

/** Consommation d'eau recue. Sans date : aujourd'hui ; sans source : MANUEL. */
public record SuiviHydratationRequest(
        @NotBlank String utilisateurId,
        @NotNull @Positive Double quantiteEau,
        LocalDate date,
        String source
) {
}
