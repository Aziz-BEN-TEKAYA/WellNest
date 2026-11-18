package tn.esprit.ms2.service;

import tn.esprit.ms2.dto.MobilityAlertResponse;
import tn.esprit.ms2.entity.enums.AlertStatus;

import java.util.List;

public interface MobilityAlertQueryService {

    /** Alertes d'un utilisateur, de la plus recente a la plus ancienne ; etat facultatif. */
    List<MobilityAlertResponse> findByUser(Long userId, AlertStatus status);

    MobilityAlertResponse findById(Long id);
}
