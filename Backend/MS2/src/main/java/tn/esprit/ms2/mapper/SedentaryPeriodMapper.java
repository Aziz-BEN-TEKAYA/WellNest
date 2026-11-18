package tn.esprit.ms2.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.ms2.dto.SedentaryPeriodRequest;
import tn.esprit.ms2.dto.SedentaryPeriodResponse;
import tn.esprit.ms2.entity.SedentaryPeriod;

@Component
public class SedentaryPeriodMapper {

    public SedentaryPeriod toEntity(SedentaryPeriodRequest request, int durationMinutes) {
        SedentaryPeriod period = new SedentaryPeriod();
        updateEntity(period, request, durationMinutes);
        return period;
    }

    public void updateEntity(SedentaryPeriod period, SedentaryPeriodRequest request, int durationMinutes) {
        period.setUserId(request.userId());
        period.setStartTime(request.startTime());
        period.setEndTime(request.endTime());
        period.setDurationMinutes(durationMinutes);
    }

    public SedentaryPeriodResponse toResponse(SedentaryPeriod period) {
        return new SedentaryPeriodResponse(
                period.getId(),
                period.getUserId(),
                period.getStartTime(),
                period.getEndTime(),
                period.getDurationMinutes());
    }
}
