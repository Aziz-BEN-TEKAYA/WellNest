package tn.esprit.ms1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class ObjectifNutritionnel {

    @Id
    private String id = UUID.randomUUID().toString();

    @NotBlank
    private String utilisateurId;

    @NotNull @Positive
    private Double objectifCalories;

    @NotNull @Positive
    private Double objectifEau;

    @NotNull
    private LocalDate dateDebut;

    private LocalDate dateFin;

    /** ACTIF, ATTEINT ou ABANDONNE. */
    private String statut = "ACTIF";

    public void modifierObjectif(Double calories, Double eau) {
        if (calories != null) this.objectifCalories = calories;
        if (eau != null) this.objectifEau = eau;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(String utilisateurId) { this.utilisateurId = utilisateurId; }
    public Double getObjectifCalories() { return objectifCalories; }
    public void setObjectifCalories(Double objectifCalories) { this.objectifCalories = objectifCalories; }
    public Double getObjectifEau() { return objectifEau; }
    public void setObjectifEau(Double objectifEau) { this.objectifEau = objectifEau; }
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}