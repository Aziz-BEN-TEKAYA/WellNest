package tn.esprit.ms2.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.ms2.dto.DailySummaryRequest;
import tn.esprit.ms2.dto.DailySummaryResponse;
import tn.esprit.ms2.entity.DailyActivitySummary;
import tn.esprit.ms2.entity.enums.DataSource;

@Component
public class DailySummaryMapper {

    public DailyActivitySummary toEntity(DailySummaryRequest request) {
        DailyActivitySummary summary = new DailyActivitySummary();
        updateEntity(summary, request);
        return summary;
    }

    public void updateEntity(DailyActivitySummary summary, DailySummaryRequest request) {
        summary.setUserId(request.userId());
        summary.setDate(request.date());
        summary.setSteps(request.steps());
        summary.setDistanceMeters(request.distanceMeters() != null ? request.distanceMeters() : 0);
        summary.setActiveMinutes(request.activeMinutes() != null ? request.activeMinutes() : 0);
        summary.setSedentaryMinutes(request.sedentaryMinutes() != null ? request.sedentaryMinutes() : 0);
        summary.setSource(request.source() != null ? request.source() : DataSource.MANUAL);
    }

    public DailySummaryResponse toResponse(DailyActivitySummary summary) {
        return new DailySummaryResponse(
                summary.getId(),
                summary.getUserId(),
                summary.getDate(),
                summary.getSteps(),
                summary.getDistanceMeters(),
                summary.getActiveMinutes(),
                summary.getSedentaryMinutes(),
                summary.getSource());
    }
}
