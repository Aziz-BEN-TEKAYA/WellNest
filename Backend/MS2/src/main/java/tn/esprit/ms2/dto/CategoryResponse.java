package tn.esprit.ms2.dto;

import tn.esprit.ms2.entity.enums.IntensityLevel;

public record CategoryResponse(
        Long id,
        String name,
        String description,
        String icon,
        IntensityLevel defaultIntensity,
        int activityTypeCount
) {
}
