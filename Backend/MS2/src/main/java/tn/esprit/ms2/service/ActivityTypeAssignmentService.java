package tn.esprit.ms2.service;

import tn.esprit.ms2.dto.ActivityTypeResponse;

/**
 * Affectation d'un type d'activite a une categorie (ISP : separee du CRUD).
 * Pas de desaffectation : un type appartient toujours a une categorie (association obligatoire).
 */
public interface ActivityTypeAssignmentService {

    ActivityTypeResponse assignToCategory(Long activityTypeId, Long categoryId);
}
