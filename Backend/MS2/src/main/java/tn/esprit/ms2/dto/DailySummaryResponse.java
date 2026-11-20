package tn.esprit.ms2.dto;

import tn.esprit.ms2.entity.enums.DataSource;

import java.time.LocalDate;

public record DailySummaryResponse(
        Long id,
        Long userId,
        LocalDate date,
        int steps,
        double distanceMeters,
        int activeMinutes,
        int sedentaryMinutes,
        DataSource source
) {
}
