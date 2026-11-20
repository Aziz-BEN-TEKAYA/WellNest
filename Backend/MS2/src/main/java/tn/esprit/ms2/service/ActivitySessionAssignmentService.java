package tn.esprit.ms2.service;

import tn.esprit.ms2.dto.ActivitySessionResponse;

/**
 * Affectation d'une seance a un type d'activite (ISP : separee du CRUD).
 * Pas de desaffectation : une seance a toujours un type (association obligatoire).
 */
public interface ActivitySessionAssignmentService {

    ActivitySessionResponse assignToActivityType(Long sessionId, Long activityTypeId);
}
