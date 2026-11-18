package tn.esprit.ms2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ms2.entity.MobilityAlert;
import tn.esprit.ms2.entity.enums.AlertStatus;

import java.util.List;

public interface MobilityAlertRepository extends JpaRepository<MobilityAlert, Long> {

    List<MobilityAlert> findByUserIdOrderByDetectedOnDesc(Long userId);

    List<MobilityAlert> findByUserIdAndStatusOrderByDetectedOnDesc(Long userId, AlertStatus status);
}
