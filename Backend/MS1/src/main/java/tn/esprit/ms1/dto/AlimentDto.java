package tn.esprit.ms1.dto;

import jakarta.validation.constraints.NotBlank;

/** Aliment d'un repas (utilise en entree et en sortie). */
public record AlimentDto(
        @NotBlank String nom,
        Double quantite,
        Double calories
) {
}
