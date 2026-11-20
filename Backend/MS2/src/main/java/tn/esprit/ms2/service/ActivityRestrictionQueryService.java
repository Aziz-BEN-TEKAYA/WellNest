package tn.esprit.ms2.service;

import tn.esprit.ms2.dto.ActivityRestrictionResponse;

import java.util.List;

public interface ActivityRestrictionQueryService {

    List<ActivityRestrictionResponse> findByUser(Long userId);

    ActivityRestrictionResponse findById(Long id);
}
