package tn.esprit.ms1.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tn.esprit.ms1.entity.TypeRepas;

import java.time.LocalDate;
import java.util.List;

/** Donnees recues pour creer ou modifier un repas. L'id et les calories totales sont calcules par le serveur. */
public record RepasRequest(
        @NotBlank String utilisateurId,
        @NotBlank String nom,
        @NotNull TypeRepas type,
        @NotNull LocalDate date,
        @Valid List<AlimentDto> aliments
) {
}
