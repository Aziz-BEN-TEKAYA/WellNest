package tn.esprit.ms6.dto;

import tn.esprit.ms6.enums.AutonomyNeed;
import tn.esprit.ms6.enums.Obstacle;
import tn.esprit.ms6.enums.UsageDuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        String image,
        BigDecimal weightKg,
        BigDecimal widthCm,
        BigDecimal heightCm,
        BigDecimal depthCm,
        BigDecimal foldedWidthCm,
        BigDecimal minimumDoorWidthCm,
        String material,
        Integer warrantyMonths,
        Integer maxLoadKg,
        boolean installationRequired,
        UsageDuration usageDuration,
        LocalDateTime createdAt,
        Long categoryId,
        String categoryName,
        Set<Obstacle> obstacles,
        Set<AutonomyNeed> needs
) {
}