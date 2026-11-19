package tn.esprit.ms1.repository;

import tn.esprit.ms1.entity.Recommandation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RecommandationRepository extends JpaRepository<Recommandation, String> {
    List<Recommandation> findByUtilisateurId(String utilisateurId);
    boolean existsByUtilisateurIdAndContenuAndDateCreation(String utilisateurId, String contenu, LocalDate date);
}
