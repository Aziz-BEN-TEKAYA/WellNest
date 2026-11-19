package tn.esprit.ms1.service;

import tn.esprit.ms1.exception.NotFoundException;
import tn.esprit.ms1.entity.ObjectifNutritionnel;
import tn.esprit.ms1.repository.ObjectifNutritionnelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObjectifService {

    private final ObjectifNutritionnelRepository repo;

    public ObjectifService(ObjectifNutritionnelRepository repo) { this.repo = repo; }

    public ObjectifNutritionnel creer(ObjectifNutritionnel o) {
        if (o.getStatut() == null) o.setStatut("ACTIF");
        return repo.save(o);
    }

    public List<ObjectifNutritionnel> lister(String utilisateurId) {
        return utilisateurId != null ? repo.findByUtilisateurId(utilisateurId) : repo.findAll();
    }

    public ObjectifNutritionnel trouver(String id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Objectif", id));
    }

    public ObjectifNutritionnel modifier(String id, ObjectifNutritionnel nouveau) {
        ObjectifNutritionnel o = trouver(id);
        o.modifierObjectif(nouveau.getObjectifCalories(), nouveau.getObjectifEau());
        o.setDateDebut(nouveau.getDateDebut());
        o.setDateFin(nouveau.getDateFin());
        if (nouveau.getStatut() != null) o.setStatut(nouveau.getStatut());
        return repo.save(o);
    }

    public void supprimer(String id) { repo.delete(trouver(id)); }
}
