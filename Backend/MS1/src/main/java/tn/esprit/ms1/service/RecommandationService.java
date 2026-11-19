package tn.esprit.ms1.service;

import tn.esprit.ms1.exception.NotFoundException;
import tn.esprit.ms1.entity.ObjectifNutritionnel;
import tn.esprit.ms1.entity.Recommandation;
import tn.esprit.ms1.entity.Repas;
import tn.esprit.ms1.entity.SuiviHydratation;
import tn.esprit.ms1.repository.ObjectifNutritionnelRepository;
import tn.esprit.ms1.repository.RecommandationRepository;
import tn.esprit.ms1.repository.RepasRepository;
import tn.esprit.ms1.repository.SuiviHydratationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecommandationService {

    private final RecommandationRepository repo;
    private final ObjectifNutritionnelRepository objectifs;
    private final RepasRepository repas;
    private final SuiviHydratationRepository eau;

    public RecommandationService(RecommandationRepository repo, ObjectifNutritionnelRepository objectifs,
                                 RepasRepository repas, SuiviHydratationRepository eau) {
        this.repo = repo;
        this.objectifs = objectifs;
        this.repas = repas;
        this.eau = eau;
    }

    /**
     * genererRecommandation() du diagramme de classes :
     * compare la journée (repas + eau) à l'objectif actif et crée des conseils.
     */
    public void genererRecommandation(String utilisateurId) {
        LocalDate today = LocalDate.now();
        ObjectifNutritionnel objectif = objectifs
                .findFirstByUtilisateurIdAndStatut(utilisateurId, "ACTIF").orElse(null);
        if (objectif == null) return;

        double calories = repas.findByUtilisateurIdAndDate(utilisateurId, today).stream()
                .mapToDouble(Repas::getCaloriesTotales).sum();
        double eauMl = eau.findByUtilisateurIdAndDate(utilisateurId, today).stream()
                .mapToDouble(SuiviHydratation::getQuantiteEau).sum();

        if (eauMl < objectif.getObjectifEau()) {
            creer(utilisateurId, "HYDRATATION", "Il vous reste " + Math.round(objectif.getObjectifEau() - eauMl)
                    + " ml d'eau à boire pour atteindre votre objectif du jour.", today);
        }
        if (calories < objectif.getObjectifCalories()) {
            creer(utilisateurId, "NUTRITION", "Il vous reste " + Math.round(objectif.getObjectifCalories() - calories)
                    + " kcal à consommer aujourd'hui : pensez à un repas équilibré.", today);
        } else if (calories > objectif.getObjectifCalories()) {
            creer(utilisateurId, "NUTRITION", "Votre objectif calorique est dépassé de "
                    + Math.round(calories - objectif.getObjectifCalories()) + " kcal : privilégiez un dîner léger.", today);
        }
    }

    private void creer(String utilisateurId, String categorie, String contenu, LocalDate date) {
        if (repo.existsByUtilisateurIdAndContenuAndDateCreation(utilisateurId, contenu, date)) return;
        Recommandation r = new Recommandation();
        r.setUtilisateurId(utilisateurId);
        r.setCategorie(categorie);
        r.setContenu(contenu);
        r.setDateCreation(date);
        repo.save(r);
    }

    public List<Recommandation> lister(String utilisateurId) {
        if (utilisateurId == null) return repo.findAll();
        genererRecommandation(utilisateurId);
        return repo.findByUtilisateurId(utilisateurId);
    }

    public Recommandation trouver(String id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Recommandation", id));
    }
}
