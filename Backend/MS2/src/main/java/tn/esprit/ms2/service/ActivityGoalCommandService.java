package tn.esprit.ms2.service;

import tn.esprit.ms2.dto.ActivityGoalRequest;
import tn.esprit.ms2.dto.ActivityGoalResponse;

public interface ActivityGoalCommandService {

    ActivityGoalResponse create(ActivityGoalRequest request);

    ActivityGoalResponse update(Long id, ActivityGoalRequest request);

    void delete(Long id);
}
