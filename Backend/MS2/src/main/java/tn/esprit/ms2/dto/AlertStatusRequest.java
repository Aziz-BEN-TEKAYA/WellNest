package tn.esprit.ms2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import tn.esprit.ms2.entity.enums.AlertStatus;

@Schema(description = "Changement d'etat d'une alerte")
public record AlertStatusRequest(

        @Schema(example = "ACKNOWLEDGED")
        @NotNull(message = "L'etat est obligatoire")
        AlertStatus status
) {
}
