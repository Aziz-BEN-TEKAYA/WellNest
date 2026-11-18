package tn.esprit.ms2.service;

import tn.esprit.ms2.dto.ActivityTypeResponse;
import tn.esprit.ms2.entity.enums.IntensityLevel;

import java.util.List;

/** Consultation et filtrage des types d'activite. */
public interface ActivityTypeQueryService {

    /** Chaque critere null est ignore. */
    List<ActivityTypeResponse> search(Long categoryId, IntensityLevel intensity, Boolean suitableForSeniors);

    List<ActivityTypeResponse> findByCategory(Long categoryId);

    ActivityTypeResponse findById(Long id);
}
