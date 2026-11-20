package tn.esprit.ms1.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.ms1.dto.SuiviHydratationRequest;
import tn.esprit.ms1.dto.SuiviHydratationResponse;
import tn.esprit.ms1.entity.SuiviHydratation;

/** Conversions SuiviHydratation <-> DTO. Les valeurs par defaut (date, source) restent celles de l'entite. */
@Component
public class SuiviHydratationMapper {

    public SuiviHydratation toEntity(SuiviHydratationRequest request) {
        SuiviHydratation suivi = new SuiviHydratation();
        suivi.setUtilisateurId(request.utilisateurId());
        suivi.setQuantiteEau(request.quantiteEau());
        if (request.date() != null) {
            suivi.setDate(request.date());
        }
        if (request.source() != null) {
            suivi.setSource(request.source());
        }
        return suivi;
    }

    public SuiviHydratationResponse toResponse(SuiviHydratation suivi) {
        return new SuiviHydratationResponse(
                suivi.getId(),
                suivi.getUtilisateurId(),
                suivi.getQuantiteEau(),
                suivi.getDate(),
                suivi.getSource());
    }
}
