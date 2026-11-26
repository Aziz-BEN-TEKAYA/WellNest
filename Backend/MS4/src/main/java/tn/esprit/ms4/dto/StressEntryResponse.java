package tn.esprit.ms4.dto;

import java.time.LocalDateTime;

public record StressEntryResponse(
        Long id,
        Integer stressLevel,
        String cause,
        String notes,
        LocalDateTime recordedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
