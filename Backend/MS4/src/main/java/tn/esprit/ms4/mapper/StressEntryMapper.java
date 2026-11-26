package tn.esprit.ms4.mapper;

import tn.esprit.ms4.dto.StressEntryRequest;
import tn.esprit.ms4.dto.StressEntryResponse;
import tn.esprit.ms4.entity.StressEntry;

import java.time.LocalDateTime;

public final class StressEntryMapper {

    private StressEntryMapper() {
    }

    public static void copyRequest(StressEntryRequest request, StressEntry entity, LocalDateTime now) {
        entity.setStressLevel(request.stressLevel());
        entity.setCause(request.cause());
        entity.setNotes(request.notes());
        if (request.recordedAt() != null) {
            entity.setRecordedAt(request.recordedAt());
        } else if (entity.getRecordedAt() == null) {
            entity.setRecordedAt(now);
        }
    }

    public static StressEntryResponse toResponse(StressEntry e) {
        return new StressEntryResponse(e.getId(), e.getStressLevel(), e.getCause(), e.getNotes(),
                e.getRecordedAt(), e.getCreatedAt(), e.getUpdatedAt());
    }
}
