package tn.esprit.ms2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ms2.entity.DailyActivitySummary;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyActivitySummaryRepository extends JpaRepository<DailyActivitySummary, Long> {

    Optional<DailyActivitySummary> findByUserIdAndDate(Long userId, LocalDate date);

    boolean existsByUserIdAndDate(Long userId, LocalDate date);

    boolean existsByUserIdAndDateAndIdNot(Long userId, LocalDate date, Long id);

    /** Sert notamment a la mediane des pas sur 28 jours. */
    List<DailyActivitySummary> findByUserIdAndDateBetweenOrderByDateAsc(Long userId, LocalDate from, LocalDate to);
}
