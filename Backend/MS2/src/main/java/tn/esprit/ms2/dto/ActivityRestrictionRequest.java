package tn.esprit.ms2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tn.esprit.ms2.entity.enums.IntensityLevel;
import tn.esprit.ms2.entity.enums.RestrictionOrigin;

import java.util.Set;

@Schema(description = "Consigne limitant l'activite (envoyee par l'utilisateur ou par Health Profile)")
public record ActivityRestrictionRequest(

        @Schema(example = "1")
        @NotNull(message = "L'utilisateur est obligatoire")
        Long userId,

        @Schema(example = "MODERATE", description = "Facultative : intensite maximale autorisee")
        IntensityLevel maxIntensity,

        @Schema(example = "PROFESSIONAL")
        @NotNull(message = "L'origine est obligatoire")
        RestrictionOrigin origin,

        @Schema(example = "Pas d'activite aquatique apres l'operation de la hanche")
        @Size(max = 1000, message = "Les notes ne doivent pas depasser 1000 caracteres")
        String notes,

        @Schema(example = "[5]", description = "Identifiants des categories exclues (facultatif)")
        Set<Long> excludedCategoryIds
) {
}
