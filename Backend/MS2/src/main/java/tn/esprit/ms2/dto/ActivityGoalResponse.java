package tn.esprit.ms2.dto;

import tn.esprit.ms2.entity.enums.GoalType;

import java.time.LocalDate;

public record ActivityGoalResponse(
        Long id,
        Long userId,
        GoalType goalType,
        int targetValue,
        LocalDate startDate,
        LocalDate endDate,
        boolean active
) {
}
