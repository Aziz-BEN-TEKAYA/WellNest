package tn.esprit.ms2.service;

import tn.esprit.ms2.dto.DailySummaryResponse;

import java.time.LocalDate;
import java.util.List;

public interface DailySummaryQueryService {

    /** Resumes d'un utilisateur, filtres par periode (bornes facultatives). */
    List<DailySummaryResponse> findByUser(Long userId, LocalDate from, LocalDate to);

    DailySummaryResponse findById(Long id);
}
