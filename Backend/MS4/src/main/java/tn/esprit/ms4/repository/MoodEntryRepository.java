package tn.esprit.ms4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import tn.esprit.ms4.entity.MoodEntry;

import java.util.Optional;

public interface MoodEntryRepository extends JpaRepository<MoodEntry, Long>, JpaSpecificationExecutor<MoodEntry> {

    Optional<MoodEntry> findByIdAndUserId(Long id, Long userId);
}
