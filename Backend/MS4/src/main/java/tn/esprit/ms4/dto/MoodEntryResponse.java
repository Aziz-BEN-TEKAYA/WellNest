package tn.esprit.ms4.dto;

import tn.esprit.ms4.entity.enums.MoodLevel;

import java.time.LocalDateTime;

public record MoodEntryResponse(
        Long id,
        MoodLevel moodLevel,
        Integer moodScore,
        String description,
        LocalDateTime recordedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
