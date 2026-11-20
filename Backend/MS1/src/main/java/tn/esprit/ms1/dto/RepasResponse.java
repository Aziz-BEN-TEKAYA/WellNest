package tn.esprit.ms1.dto;

import tn.esprit.ms1.entity.TypeRepas;

import java.time.LocalDate;
import java.util.List;

public record RepasResponse(
        String id,
        String utilisateurId,
        String nom,
        TypeRepas type,
        LocalDate date,
        List<AlimentDto> aliments,
        Double caloriesTotales
) {
}
