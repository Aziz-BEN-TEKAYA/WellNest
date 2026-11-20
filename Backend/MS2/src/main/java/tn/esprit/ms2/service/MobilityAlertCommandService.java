package tn.esprit.ms2.service;

import tn.esprit.ms2.dto.MobilityAlertRequest;
import tn.esprit.ms2.dto.MobilityAlertResponse;
import tn.esprit.ms2.entity.enums.AlertStatus;

public interface MobilityAlertCommandService {

    MobilityAlertResponse create(MobilityAlertRequest request);

    MobilityAlertResponse update(Long id, MobilityAlertRequest request);

    /** Prise en compte (ACKNOWLEDGED) ou rejet (DISMISSED) d'une alerte. */
    MobilityAlertResponse changeStatus(Long id, AlertStatus status);

    void delete(Long id);
}
