package tn.esprit.ms3.dto;

import java.time.LocalDateTime;

public record ExportRecord(
        String userId,
        LocalDateTime timestamp,
        String metric,
        Double value,
        String unit,
        String source,
        String quality) {
}