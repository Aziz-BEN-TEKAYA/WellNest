package tn.esprit.ms2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ms2.entity.ActivitySession;

import java.time.LocalDateTime;
import java.util.List;

public interface ActivitySessionRepository extends JpaRepository<ActivitySession, Long> {

    boolean existsByActivityTypeId(Long activityTypeId);

    List<ActivitySession> findByUserIdOrderByStartTimeDesc(Long userId);

    List<ActivitySession> findByUserIdAndStartTimeBetweenOrderByStartTimeAsc(Long userId,
                                                                             LocalDateTime from,
                                                                             LocalDateTime to);

    List<ActivitySession> findByActivityTypeCategoryId(Long categoryId);
}
