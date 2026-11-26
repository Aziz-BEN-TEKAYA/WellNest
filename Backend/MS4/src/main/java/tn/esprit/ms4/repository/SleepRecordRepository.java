package tn.esprit.ms4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import tn.esprit.ms4.entity.SleepRecord;

import java.time.LocalDate;
import java.util.Optional;

public interface SleepRecordRepository extends JpaRepository<SleepRecord, Long>, JpaSpecificationExecutor<SleepRecord> {

    Optional<SleepRecord> findByIdAndUserId(Long id, Long userId);

    boolean existsByUserIdAndSleepDate(Long userId, LocalDate sleepDate);

    boolean existsByUserIdAndSleepDateAndIdNot(Long userId, LocalDate sleepDate, Long id);
}
