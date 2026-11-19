package tn.esprit.ms1.repository;

import tn.esprit.ms1.entity.Repas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RepasRepository extends JpaRepository<Repas, String> {
    List<Repas> findByUtilisateurId(String utilisateurId);
    List<Repas> findByUtilisateurIdAndDate(String utilisateurId, LocalDate date);
    List<Repas> findByDate(LocalDate date);
}
