package tn.esprit.ms4.dto;

import tn.esprit.ms4.entity.enums.ReminderType;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record WellnessReminderResponse(
        Long id,
        ReminderType reminderType,
        LocalTime reminderTime,
        Boolean enabled,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
