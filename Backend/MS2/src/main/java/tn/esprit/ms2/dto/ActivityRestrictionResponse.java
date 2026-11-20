package tn.esprit.ms2.dto;

import tn.esprit.ms2.entity.enums.IntensityLevel;
import tn.esprit.ms2.entity.enums.RestrictionOrigin;

import java.util.List;

public record ActivityRestrictionResponse(
        Long id,
        Long userId,
        IntensityLevel maxIntensity,
        RestrictionOrigin origin,
        String notes,
        List<CategorySummary> excludedCategories
) {
}
