package tn.esprit.ms2.service;

import tn.esprit.ms2.dto.ActivityGoalResponse;

import java.util.List;

public interface ActivityGoalQueryService {

    List<ActivityGoalResponse> findByUser(Long userId, boolean activeOnly);

    ActivityGoalResponse findById(Long id);
}
