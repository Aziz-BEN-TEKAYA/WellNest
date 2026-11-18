package tn.esprit.ms2.service;

import tn.esprit.ms2.dto.ActivitySessionRequest;
import tn.esprit.ms2.dto.ActivitySessionResponse;

public interface ActivitySessionCommandService {

    ActivitySessionResponse create(ActivitySessionRequest request);

    ActivitySessionResponse update(Long id, ActivitySessionRequest request);

    void delete(Long id);
}
