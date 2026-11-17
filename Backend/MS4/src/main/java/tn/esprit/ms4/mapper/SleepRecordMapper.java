package tn.esprit.ms4.mapper;

import tn.esprit.ms4.dto.SleepRecordRequest;
import tn.esprit.ms4.dto.SleepRecordResponse;
import tn.esprit.ms4.entity.SleepRecord;

public final class SleepRecordMapper {

    private SleepRecordMapper() {
    }

    /** Copie les champs saisis. La duree et la date de la nuit sont calculees par le service. */
    public static void copyRequest(SleepRecordRequest request, SleepRecord entity) {
        entity.setBedtime(request.bedtime());
        entity.setWakeupTime(request.wakeupTime());
        entity.setSleepQuality(request.sleepQuality());
        entity.setNightWakings(request.nightWakings() == null ? 0 : request.nightWakings());
        entity.setNotes(request.notes());
    }

    public static SleepRecordResponse toResponse(SleepRecord e) {
        return new SleepRecordResponse(e.getId(), e.getSleepDate(), e.getBedtime(), e.getWakeupTime(),
                e.getDurationMinutes(), e.getSleepQuality(), e.getNightWakings(), e.getNotes(),
                e.getCreatedAt(), e.getUpdatedAt());
    }
}
