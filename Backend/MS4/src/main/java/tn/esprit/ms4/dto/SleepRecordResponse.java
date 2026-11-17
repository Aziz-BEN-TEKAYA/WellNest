package tn.esprit.ms4.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SleepRecordResponse(
        Long id,
        LocalDate sleepDate,
        LocalDateTime bedtime,
        LocalDateTime wakeupTime,
        Integer durationMinutes,
        Integer sleepQuality,
        Integer nightWakings,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
