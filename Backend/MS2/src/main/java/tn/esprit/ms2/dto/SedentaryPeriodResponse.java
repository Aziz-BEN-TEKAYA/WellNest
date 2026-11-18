package tn.esprit.ms2.dto;

import java.time.LocalDateTime;

public record SedentaryPeriodResponse(
        Long id,
        Long userId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        int durationMinutes
) {
}
