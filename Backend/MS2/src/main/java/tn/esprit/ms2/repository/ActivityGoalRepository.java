package tn.esprit.ms2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ms2.entity.ActivityGoal;

import java.util.List;

public interface ActivityGoalRepository extends JpaRepository<ActivityGoal, Long> {

    List<ActivityGoal> findByUserIdAndActiveTrue(Long userId);

    List<ActivityGoal> findByUserId(Long userId);
}
