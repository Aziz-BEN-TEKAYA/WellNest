package tn.esprit.ms2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tn.esprit.ms2.entity.enums.IntensityLevel;

@Schema(description = "Donnees pour creer ou modifier un type d'activite")
public record ActivityTypeRequest(

        @Schema(example = "Marche nordique")
        @NotBlank(message = "Le nom est obligatoire")
        @Size(max = 100, message = "Le nom ne doit pas depasser 100 caracteres")
        String name,

        @Schema(example = "MODERATE")
        @NotNull(message = "L'intensite est obligatoire")
        IntensityLevel intensity,

        @Schema(example = "4.8", description = "Equivalent metabolique (MET)")
        @NotNull(message = "La valeur MET est obligatoire")
        @DecimalMin(value = "1.0", message = "La valeur MET doit etre au moins 1.0")
        @DecimalMax(value = "20.0", message = "La valeur MET ne doit pas depasser 20.0")
        Double metValue,

        @Schema(example = "true", description = "Facultatif, false par defaut")
        Boolean suitableForSeniors,

        @Size(max = 500, message = "La description ne doit pas depasser 500 caracteres")
        String description,

        @Schema(example = "1")
        @NotNull(message = "La categorie est obligatoire")
        Long categoryId
) {
}
