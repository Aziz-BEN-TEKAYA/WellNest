package tn.esprit.ms1.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.ms1.dto.RecommandationResponse;
import tn.esprit.ms1.entity.Recommandation;

@Component
public class RecommandationMapper {

    public RecommandationResponse toResponse(Recommandation recommandation) {
        return new RecommandationResponse(
                recommandation.getId(),
                recommandation.getUtilisateurId(),
                recommandation.getCategorie(),
                recommandation.getContenu(),
                recommandation.getDateCreation());
    }
}
