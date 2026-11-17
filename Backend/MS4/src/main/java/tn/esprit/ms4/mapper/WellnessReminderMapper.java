package tn.esprit.ms4.mapper;

import tn.esprit.ms4.dto.WellnessReminderRequest;
import tn.esprit.ms4.dto.WellnessReminderResponse;
import tn.esprit.ms4.entity.WellnessReminder;

public final class WellnessReminderMapper {

    private WellnessReminderMapper() {
    }

    public static void copyRequest(WellnessReminderRequest request, WellnessReminder entity) {
        entity.setReminderType(request.reminderType());
        entity.setReminderTime(request.reminderTime().withSecond(0).withNano(0));
        if (request.enabled() != null) {
            entity.setEnabled(request.enabled());
        } else if (entity.getEnabled() == null) {
            entity.setEnabled(true);
        }
    }

    public static WellnessReminderResponse toResponse(WellnessReminder e) {
        return new WellnessReminderResponse(e.getId(), e.getReminderType(), e.getReminderTime(),
                e.getEnabled(), e.getCreatedAt(), e.getUpdatedAt());
    }
}
