package tn.esprit.ms4.dto;

import tn.esprit.ms4.entity.enums.WellnessActivityType;

import java.time.LocalDateTime;

public record WellnessActivityResponse(
        Long id,
        WellnessActivityType activityType,
        Integer durationMinutes,
        LocalDateTime activityDate,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
