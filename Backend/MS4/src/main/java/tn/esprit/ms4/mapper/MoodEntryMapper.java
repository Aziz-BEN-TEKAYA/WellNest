package tn.esprit.ms4.mapper;

import tn.esprit.ms4.dto.MoodEntryRequest;
import tn.esprit.ms4.dto.MoodEntryResponse;
import tn.esprit.ms4.entity.MoodEntry;

import java.time.LocalDateTime;

public final class MoodEntryMapper {

    private MoodEntryMapper() {
    }

    public static void copyRequest(MoodEntryRequest request, MoodEntry entity, LocalDateTime now) {
        entity.setMoodLevel(request.moodLevel());
        entity.setMoodScore(request.moodLevel().getScore());
        entity.setDescription(request.description());
        if (request.recordedAt() != null) {
            entity.setRecordedAt(request.recordedAt());
        } else if (entity.getRecordedAt() == null) {
            entity.setRecordedAt(now);
        }
    }

    public static MoodEntryResponse toResponse(MoodEntry e) {
        return new MoodEntryResponse(e.getId(), e.getMoodLevel(), e.getMoodScore(), e.getDescription(),
                e.getRecordedAt(), e.getCreatedAt(), e.getUpdatedAt());
    }
}
