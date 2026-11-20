package tn.esprit.ms1.dto;

import java.time.LocalDate;

/** Les recommandations sont generees par le serveur : pas de DTO d'entree. */
public record RecommandationResponse(
        String id,
        String utilisateurId,
        String categorie,
        String contenu,
        LocalDate dateCreation
) {
}
