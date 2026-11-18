package tn.esprit.ms2.service;

import tn.esprit.ms2.dto.ActivityRestrictionRequest;
import tn.esprit.ms2.dto.ActivityRestrictionResponse;

public interface ActivityRestrictionCommandService {

    ActivityRestrictionResponse create(ActivityRestrictionRequest request);

    ActivityRestrictionResponse update(Long id, ActivityRestrictionRequest request);

    void delete(Long id);
}
