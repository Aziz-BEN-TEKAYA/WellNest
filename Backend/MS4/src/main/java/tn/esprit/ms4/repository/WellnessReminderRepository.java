package tn.esprit.ms4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ms4.entity.WellnessReminder;
import tn.esprit.ms4.entity.enums.ReminderType;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface WellnessReminderRepository extends JpaRepository<WellnessReminder, Long> {

    Optional<WellnessReminder> findByIdAndUserId(Long id, Long userId);

    List<WellnessReminder> findByUserIdOrderByReminderTimeAsc(Long userId);

    boolean existsByUserIdAndReminderTypeAndReminderTime(Long userId, ReminderType type, LocalTime time);

    boolean existsByUserIdAndReminderTypeAndReminderTimeAndIdNot(Long userId, ReminderType type, LocalTime time, Long id);
}
