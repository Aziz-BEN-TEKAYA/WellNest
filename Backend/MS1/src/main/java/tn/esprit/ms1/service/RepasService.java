package tn.esprit.ms1.service;

import tn.esprit.ms1.exception.NotFoundException;
import tn.esprit.ms1.entity.Repas;
import tn.esprit.ms1.repository.RepasRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RepasService {

    private final RepasRepository repo;

    public RepasService(RepasRepository repo) { this.repo = repo; }

    public Repas ajouter(Repas r) {
        r.calculerCalories();
        return repo.save(r);
    }

    public List<Repas> lister(String utilisateurId, LocalDate date) {
        if (utilisateurId != null && date != null) return repo.findByUtilisateurIdAndDate(utilisateurId, date);
        if (utilisateurId != null) return repo.findByUtilisateurId(utilisateurId);
        if (date != null) return repo.findByDate(date);
        return repo.findAll();
    }

    public Repas trouver(String id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Repas", id));
    }

    public Repas modifier(String id, Repas nouveau) {
        Repas r = trouver(id);
        r.setUtilisateurId(nouveau.getUtilisateurId());
        r.setNom(nouveau.getNom());
        r.setType(nouveau.getType());
        r.setDate(nouveau.getDate());
        r.setAliments(nouveau.getAliments());
        r.calculerCalories();
        return repo.save(r);
    }

    public void supprimer(String id) {
        repo.delete(trouver(id));
    }
}
