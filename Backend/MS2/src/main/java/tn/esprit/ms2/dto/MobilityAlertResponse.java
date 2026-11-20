package tn.esprit.ms2.dto;

import tn.esprit.ms2.entity.enums.AlertStatus;
import tn.esprit.ms2.entity.enums.AlertType;

import java.time.LocalDate;

public record MobilityAlertResponse(
        Long id,
        Long userId,
        LocalDate detectedOn,
        AlertType alertType,
        double baselineValue,
        double observedValue,
        double deviationPercent,
        AlertStatus status
) {
}
