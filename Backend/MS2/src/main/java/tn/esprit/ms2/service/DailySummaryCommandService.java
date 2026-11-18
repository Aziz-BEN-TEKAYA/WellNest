package tn.esprit.ms2.service;

import tn.esprit.ms2.dto.DailySummaryRequest;
import tn.esprit.ms2.dto.DailySummaryResponse;

public interface DailySummaryCommandService {

    DailySummaryResponse create(DailySummaryRequest request);

    DailySummaryResponse update(Long id, DailySummaryRequest request);

    void delete(Long id);
}
