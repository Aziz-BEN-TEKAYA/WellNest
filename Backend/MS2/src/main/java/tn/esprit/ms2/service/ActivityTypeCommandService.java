package tn.esprit.ms2.service;

import tn.esprit.ms2.dto.ActivityTypeRequest;
import tn.esprit.ms2.dto.ActivityTypeResponse;

/** Creation, modification et suppression des types d'activite. */
public interface ActivityTypeCommandService {

    ActivityTypeResponse create(ActivityTypeRequest request);

    ActivityTypeResponse update(Long id, ActivityTypeRequest request);

    void delete(Long id);
}
