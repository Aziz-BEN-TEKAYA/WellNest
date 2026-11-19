package tn.esprit.ms1.service;

import tn.esprit.ms1.entity.ObjectifNutritionnel;
import tn.esprit.ms1.entity.SuiviHydratation;
import tn.esprit.ms1.repository.ObjectifNutritionnelRepository;
import tn.esprit.ms1.repository.SuiviHydratationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class HydratationService {

    private final SuiviHydratationRepository repo;
    private final ObjectifNutritionnelRepository objectifs;

    public HydratationService(SuiviHydratationRepository repo, ObjectifNutritionnelRepository objectifs) {
        this.repo = repo;
        this.objectifs = objectifs;
    }

    /** enregistrerConsommation() du diagramme de classes. */
    public SuiviHydratation enregistrerConsommation(SuiviHydratation s) {
        if (s.getDate() == null) s.setDate(LocalDate.now());
        return repo.save(s);
    }

    public List<SuiviHydratation> lister(String utilisateurId, LocalDate date) {
        if (utilisateurId != null && date != null) return repo.findByUtilisateurIdAndDate(utilisateurId, date);
        if (utilisateurId != null) return repo.findByUtilisateurId(utilisateurId);
        if (date != null) return repo.findByDate(date);
        return repo.findAll();
    }

    /** Bilan hydrique d'une journée : total bu, objectif et pourcentage atteint. */
    public Map<String, Object> bilan(String utilisateurId, LocalDate date) {
        LocalDate jour = date != null ? date : LocalDate.now();
        double total = repo.findByUtilisateurIdAndDate(utilisateurId, jour).stream()
                .mapToDouble(SuiviHydratation::getQuantiteEau).sum();
        Double objectif = objectifs.findFirstByUtilisateurIdAndStatut(utilisateurId, "ACTIF")
                .map(ObjectifNutritionnel::getObjectifEau).orElse(null);

        Map<String, Object> bilan = new LinkedHashMap<>();
        bilan.put("utilisateurId", utilisateurId);
        bilan.put("date", jour);
        bilan.put("totalEauMl", total);
        bilan.put("objectifEauMl", objectif);
        bilan.put("pourcentage", objectif == null ? null : Math.round(total / objectif * 1000) / 10.0);
        return bilan;
    }
}
