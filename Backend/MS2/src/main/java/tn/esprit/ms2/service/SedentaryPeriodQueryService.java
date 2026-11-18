package tn.esprit.ms2.service;

import tn.esprit.ms2.dto.SedentaryPeriodResponse;

import java.time.LocalDate;
import java.util.List;

public interface SedentaryPeriodQueryService {

    List<SedentaryPeriodResponse> findByUser(Long userId, LocalDate from, LocalDate to);

    SedentaryPeriodResponse findById(Long id);
}
