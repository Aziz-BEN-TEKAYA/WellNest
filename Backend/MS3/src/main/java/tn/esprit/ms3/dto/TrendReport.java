package tn.esprit.ms3.dto;

import tn.esprit.ms3.profile.MeasurementType;

public record TrendReport(
        MeasurementType type,
        Double averageValue,
        Double variation,
        String period) {
}