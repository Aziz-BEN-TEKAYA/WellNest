package tn.esprit.ms2.service;

import tn.esprit.ms2.dto.ActivityRestrictionResponse;

/** Affectation et desaffectation des categories exclues par une consigne (association 0..* -- 0..*). */
public interface RestrictionCategoryAssignmentService {

    ActivityRestrictionResponse assignExcludedCategory(Long restrictionId, Long categoryId);

    ActivityRestrictionResponse unassignExcludedCategory(Long restrictionId, Long categoryId);
}
