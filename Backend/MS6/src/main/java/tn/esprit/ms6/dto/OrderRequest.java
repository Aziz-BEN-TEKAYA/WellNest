package tn.esprit.ms6.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OrderRequest(
        @NotNull(message = "Le panier est obligatoire")
        Long cartId,

        /** Facultatif : personne qui reçoit l'équipement (ex. un aidant commande pour un senior). */
        @Size(max = 100, message = "L'identifiant du bénéficiaire ne doit pas dépasser 100 caractères")
        String beneficiaryUserId
) {
}