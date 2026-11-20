package tn.esprit.ms1.repository;

import tn.esprit.ms1.entity.ObjectifNutritionnel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ObjectifNutritionnelRepository extends JpaRepository<ObjectifNutritionnel, String> {
    List<ObjectifNutritionnel> findByUtilisateurId(String utilisateurId);
    Optional<ObjectifNutritionnel> findFirstByUtilisateurIdAndStatut(String utilisateurId, String statut);
}
