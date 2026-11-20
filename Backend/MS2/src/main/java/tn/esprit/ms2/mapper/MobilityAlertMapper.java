package tn.esprit.ms2.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.ms2.dto.MobilityAlertRequest;
import tn.esprit.ms2.dto.MobilityAlertResponse;
import tn.esprit.ms2.entity.MobilityAlert;
import tn.esprit.ms2.entity.enums.AlertStatus;

/** Conversions MobilityAlert <-> DTO. L'ecart en % est calcule par le service. */
@Component
public class MobilityAlertMapper {

    public MobilityAlert toEntity(MobilityAlertRequest request, double deviationPercent) {
        MobilityAlert alert = new MobilityAlert();
        updateEntity(alert, request, deviationPercent);
        return alert;
    }

    public void updateEntity(MobilityAlert alert, MobilityAlertRequest request, double deviationPercent) {
        alert.setUserId(request.userId());
        alert.setDetectedOn(request.detectedOn());
        alert.setAlertType(request.alertType());
        alert.setBaselineValue(request.baselineValue());
        alert.setObservedValue(request.observedValue());
        alert.setDeviationPercent(deviationPercent);
        alert.setStatus(request.status() != null ? request.status() : AlertStatus.NEW);
    }

    public MobilityAlertResponse toResponse(MobilityAlert alert) {
        return new MobilityAlertResponse(
                alert.getId(),
                alert.getUserId(),
                alert.getDetectedOn(),
                alert.getAlertType(),
                alert.getBaselineValue(),
                alert.getObservedValue(),
                alert.getDeviationPercent(),
                alert.getStatus());
    }
}
