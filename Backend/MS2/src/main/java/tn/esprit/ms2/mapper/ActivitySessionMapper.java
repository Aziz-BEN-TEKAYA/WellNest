package tn.esprit.ms2.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.ms2.dto.ActivitySessionRequest;
import tn.esprit.ms2.dto.ActivitySessionResponse;
import tn.esprit.ms2.entity.ActivitySession;
import tn.esprit.ms2.entity.ActivityType;
import tn.esprit.ms2.entity.enums.DataSource;

/** Conversions ActivitySession <-> DTO. Le type et la duree sont resolus par le service. */
@Component
public class ActivitySessionMapper {

    public ActivitySession toEntity(ActivitySessionRequest request, ActivityType type, int durationMinutes) {
        ActivitySession session = new ActivitySession();
        updateEntity(session, request, type, durationMinutes);
        return session;
    }

    public void updateEntity(ActivitySession session, ActivitySessionRequest request, ActivityType type,
                             int durationMinutes) {
        session.setUserId(request.userId());
        session.setStartTime(request.startTime());
        session.setEndTime(request.endTime());
        session.setDurationMinutes(durationMinutes);
        session.setDistanceMeters(request.distanceMeters() != null ? request.distanceMeters() : 0);
        session.setPerceivedEffort(request.perceivedEffort() != null ? request.perceivedEffort() : 0);
        session.setNotes(request.notes());
        session.setSource(request.source() != null ? request.source() : DataSource.MANUAL);
        session.assignToActivityType(type);
    }

    public ActivitySessionResponse toResponse(ActivitySession session) {
        ActivityType type = session.getActivityType();
        return new ActivitySessionResponse(
                session.getId(),
                session.getUserId(),
                type.getId(),
                type.getName(),
                type.getCategory().getName(),
                type.getIntensity(),
                session.getStartTime(),
                session.getEndTime(),
                session.getDurationMinutes(),
                session.getDistanceMeters(),
                session.getPerceivedEffort(),
                session.getNotes(),
                session.getSource());
    }
}
