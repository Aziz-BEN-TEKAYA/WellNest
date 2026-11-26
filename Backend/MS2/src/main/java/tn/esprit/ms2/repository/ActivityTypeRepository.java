package tn.esprit.ms2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.ms2.entity.ActivityType;
import tn.esprit.ms2.entity.enums.IntensityLevel;

import java.util.List;

public interface ActivityTypeRepository extends JpaRepository<ActivityType, Long> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    boolean existsByCategoryId(Long categoryId);

    long countBySuitableForSeniorsTrue();

    List<ActivityType> findByCategoryIdOrderByNameAsc(Long categoryId);

    /** Filtrage : chaque critere null est ignore. */
    @Query("""
            select t from ActivityType t
            where (:categoryId is null or t.category.id = :categoryId)
              and (:intensity is null or t.intensity = :intensity)
              and (:suitableForSeniors is null or t.suitableForSeniors = :suitableForSeniors)
            order by t.name
            """)
    List<ActivityType> search(@Param("categoryId") Long categoryId,
                              @Param("intensity") IntensityLevel intensity,
                              @Param("suitableForSeniors") Boolean suitableForSeniors);
}
