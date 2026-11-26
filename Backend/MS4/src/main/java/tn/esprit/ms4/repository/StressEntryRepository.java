package tn.esprit.ms4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import tn.esprit.ms4.entity.StressEntry;

import java.util.Optional;

public interface StressEntryRepository extends JpaRepository<StressEntry, Long>, JpaSpecificationExecutor<StressEntry> {

    Optional<StressEntry> findByIdAndUserId(Long id, Long userId);
}
