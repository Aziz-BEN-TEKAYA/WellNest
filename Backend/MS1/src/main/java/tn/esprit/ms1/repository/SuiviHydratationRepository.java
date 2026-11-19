package tn.esprit.ms1.repository;

import tn.esprit.ms1.entity.SuiviHydratation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SuiviHydratationRepository extends JpaRepository<SuiviHydratation, String> {
    List<SuiviHydratation> findByUtilisateurId(String utilisateurId);
    List<SuiviHydratation> findByUtilisateurIdAndDate(String utilisateurId, LocalDate date);
    List<SuiviHydratation> findByDate(LocalDate date);
}
