package tn.esprit.ms1.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.ms1.dto.ObjectifNutritionnelRequest;
import tn.esprit.ms1.dto.ObjectifNutritionnelResponse;
import tn.esprit.ms1.entity.ObjectifNutritionnel;

/** Conversions ObjectifNutritionnel <-> DTO. Sans statut, l'entite garde son defaut (ACTIF). */
@Component
public class ObjectifNutritionnelMapper {

    public ObjectifNutritionnel toEntity(ObjectifNutritionnelRequest request) {
        ObjectifNutritionnel objectif = new ObjectifNutritionnel();
        objectif.setUtilisateurId(request.utilisateurId());
        objectif.setObjectifCalories(request.objectifCalories());
        objectif.setObjectifEau(request.objectifEau());
        objectif.setDateDebut(request.dateDebut());
        objectif.setDateFin(request.dateFin());
        if (request.statut() != null) {
            objectif.setStatut(request.statut());
        }
        return objectif;
    }

    public ObjectifNutritionnelResponse toResponse(ObjectifNutritionnel objectif) {
        return new ObjectifNutritionnelResponse(
                objectif.getId(),
                objectif.getUtilisateurId(),
                objectif.getObjectifCalories(),
                objectif.getObjectifEau(),
                objectif.getDateDebut(),
                objectif.getDateFin(),
                objectif.getStatut());
    }
}
