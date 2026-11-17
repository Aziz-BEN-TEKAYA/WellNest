package tn.esprit.ms4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import tn.esprit.ms4.entity.WellnessActivity;

import java.util.Optional;

public interface WellnessActivityRepository extends JpaRepository<WellnessActivity, Long>, JpaSpecificationExecutor<WellnessActivity> {

    Optional<WellnessActivity> findByIdAndUserId(Long id, Long userId);
}
