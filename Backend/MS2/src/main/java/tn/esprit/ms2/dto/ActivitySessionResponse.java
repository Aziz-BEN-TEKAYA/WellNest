package tn.esprit.ms2.dto;

import tn.esprit.ms2.entity.enums.DataSource;
import tn.esprit.ms2.entity.enums.IntensityLevel;

import java.time.LocalDateTime;

public record ActivitySessionResponse(
        Long id,
        Long userId,
        Long activityTypeId,
        String activityTypeName,
        String categoryName,
        IntensityLevel intensity,
        LocalDateTime startTime,
        LocalDateTime endTime,
        int durationMinutes,
        double distanceMeters,
        int perceivedEffort,
        String notes,
        DataSource source
) {
}
