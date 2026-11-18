package tn.esprit.ms2.service;

import tn.esprit.ms2.dto.SedentaryPeriodRequest;
import tn.esprit.ms2.dto.SedentaryPeriodResponse;

public interface SedentaryPeriodCommandService {

    SedentaryPeriodResponse create(SedentaryPeriodRequest request);

    SedentaryPeriodResponse update(Long id, SedentaryPeriodRequest request);

    void delete(Long id);
}
