package tn.esprit.ms2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ms2.entity.SedentaryPeriod;

import java.time.LocalDateTime;
import java.util.List;

public interface SedentaryPeriodRepository extends JpaRepository<SedentaryPeriod, Long> {

    List<SedentaryPeriod> findByUserIdAndStartTimeBetweenOrderByStartTimeAsc(Long userId,
                                                                             LocalDateTime from,
                                                                             LocalDateTime to);
}
