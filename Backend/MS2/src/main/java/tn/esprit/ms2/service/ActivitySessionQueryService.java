package tn.esprit.ms2.service;

import tn.esprit.ms2.dto.ActivitySessionResponse;

import java.time.LocalDate;
import java.util.List;

public interface ActivitySessionQueryService {

    /** Seances d'un utilisateur, filtrees par periode (bornes facultatives). */
    List<ActivitySessionResponse> findByUser(Long userId, LocalDate from, LocalDate to);

    ActivitySessionResponse findById(Long id);
}
