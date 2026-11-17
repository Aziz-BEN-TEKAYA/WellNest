package tn.esprit.ms4.mapper;

import tn.esprit.ms4.dto.WellnessActivityRequest;
import tn.esprit.ms4.dto.WellnessActivityResponse;
import tn.esprit.ms4.entity.WellnessActivity;

import java.time.LocalDateTime;

public final class WellnessActivityMapper {

    private WellnessActivityMapper() {
    }

    public static void copyRequest(WellnessActivityRequest request, WellnessActivity entity, LocalDateTime now) {
        entity.setActivityType(request.activityType());
        entity.setDurationMinutes(request.durationMinutes());
        entity.setDescription(request.description());
        if (request.activityDate() != null) {
            entity.setActivityDate(request.activityDate());
        } else if (entity.getActivityDate() == null) {
            entity.setActivityDate(now);
        }
    }

    public static WellnessActivityResponse toResponse(WellnessActivity e) {
        return new WellnessActivityResponse(e.getId(), e.getActivityType(), e.getDurationMinutes(),
                e.getActivityDate(), e.getDescription(), e.getCreatedAt(), e.getUpdatedAt());
    }
}
