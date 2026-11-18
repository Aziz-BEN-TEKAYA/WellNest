package tn.esprit.ms2.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.ms2.dto.ActivityTypeRequest;
import tn.esprit.ms2.dto.ActivityTypeResponse;
import tn.esprit.ms2.entity.ActivityType;
import tn.esprit.ms2.entity.Category;

/** Conversions ActivityType <-> DTO. La categorie est resolue par le service. */
@Component
public class ActivityTypeMapper {

    public ActivityType toEntity(ActivityTypeRequest request, Category category) {
        ActivityType type = new ActivityType();
        updateEntity(type, request, category);
        return type;
    }

    public void updateEntity(ActivityType type, ActivityTypeRequest request, Category category) {
        type.setName(request.name().trim());
        type.setIntensity(request.intensity());
        type.setMetValue(request.metValue());
        type.setSuitableForSeniors(Boolean.TRUE.equals(request.suitableForSeniors()));
        type.setDescription(request.description());
        type.assignToCategory(category);
    }

    public ActivityTypeResponse toResponse(ActivityType type) {
        Category category = type.getCategory();
        return new ActivityTypeResponse(
                type.getId(),
                type.getName(),
                type.getIntensity(),
                type.getMetValue(),
                type.isSuitableForSeniors(),
                type.getDescription(),
                category.getId(),
                category.getName());
    }
}
