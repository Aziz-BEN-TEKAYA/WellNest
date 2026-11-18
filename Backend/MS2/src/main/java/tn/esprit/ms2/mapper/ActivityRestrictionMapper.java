package tn.esprit.ms2.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.ms2.dto.ActivityRestrictionRequest;
import tn.esprit.ms2.dto.ActivityRestrictionResponse;
import tn.esprit.ms2.dto.CategorySummary;
import tn.esprit.ms2.entity.ActivityRestriction;
import tn.esprit.ms2.entity.Category;

import java.util.Comparator;
import java.util.Set;

/** Conversions ActivityRestriction <-> DTO. Les categories exclues sont resolues par le service. */
@Component
public class ActivityRestrictionMapper {

    public ActivityRestriction toEntity(ActivityRestrictionRequest request, Set<Category> excludedCategories) {
        ActivityRestriction restriction = new ActivityRestriction();
        updateEntity(restriction, request, excludedCategories);
        return restriction;
    }

    public void updateEntity(ActivityRestriction restriction, ActivityRestrictionRequest request,
                             Set<Category> excludedCategories) {
        restriction.setUserId(request.userId());
        restriction.setMaxIntensity(request.maxIntensity());
        restriction.setOrigin(request.origin());
        restriction.setNotes(request.notes());
        restriction.getExcludedCategories().clear();
        restriction.getExcludedCategories().addAll(excludedCategories);
    }

    public ActivityRestrictionResponse toResponse(ActivityRestriction restriction) {
        return new ActivityRestrictionResponse(
                restriction.getId(),
                restriction.getUserId(),
                restriction.getMaxIntensity(),
                restriction.getOrigin(),
                restriction.getNotes(),
                restriction.getExcludedCategories().stream()
                        .sorted(Comparator.comparing(Category::getName))
                        .map(category -> new CategorySummary(category.getId(), category.getName()))
                        .toList());
    }
}
