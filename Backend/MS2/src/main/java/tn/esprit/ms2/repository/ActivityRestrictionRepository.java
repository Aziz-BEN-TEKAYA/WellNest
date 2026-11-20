package tn.esprit.ms2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ms2.entity.ActivityRestriction;

import java.util.List;

public interface ActivityRestrictionRepository extends JpaRepository<ActivityRestriction, Long> {

    List<ActivityRestriction> findByUserId(Long userId);

    boolean existsByExcludedCategoriesId(Long categoryId);
}
