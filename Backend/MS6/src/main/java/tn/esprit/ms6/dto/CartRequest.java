package tn.esprit.ms6.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CartRequest(
        @NotBlank(message = "L'identifiant de l'utilisateur est obligatoire")
        @Size(max = 100, message = "L'identifiant ne doit pas dépasser 100 caractères")
        String userId
) {
}