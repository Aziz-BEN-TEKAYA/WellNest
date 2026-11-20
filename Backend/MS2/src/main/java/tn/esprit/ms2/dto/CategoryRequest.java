package tn.esprit.ms2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import tn.esprit.ms2.entity.enums.IntensityLevel;

@Schema(description = "Donnees pour creer ou modifier une categorie d'activites")
public record CategoryRequest(

        @Schema(example = "Marche")
        @NotBlank(message = "Le nom est obligatoire")
        @Size(max = 80, message = "Le nom ne doit pas depasser 80 caracteres")
        String name,

        @Schema(example = "Marches a l'exterieur ou en interieur")
        @Size(max = 500, message = "La description ne doit pas depasser 500 caracteres")
        String description,

        @Schema(example = "walk")
        @Size(max = 60, message = "L'icone ne doit pas depasser 60 caracteres")
        String icon,

        @Schema(example = "LOW")
        IntensityLevel defaultIntensity
) {
}
