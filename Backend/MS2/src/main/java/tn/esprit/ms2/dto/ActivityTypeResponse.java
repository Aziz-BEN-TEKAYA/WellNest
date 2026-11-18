package tn.esprit.ms2.dto;

import tn.esprit.ms2.entity.enums.IntensityLevel;

public record ActivityTypeResponse(
        Long id,
        String name,
        IntensityLevel intensity,
        double metValue,
        boolean suitableForSeniors,
        String description,
        Long categoryId,
        String categoryName
) {
}
