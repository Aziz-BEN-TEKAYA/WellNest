package tn.esprit.ms1.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Repas {

    @Id
    private String id = UUID.randomUUID().toString();

    @NotBlank
    private String utilisateurId;

    @NotBlank
    private String nom;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TypeRepas type;

    @NotNull
    private LocalDate date;

    @Valid
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Aliment> aliments = new ArrayList<>();

    private Double caloriesTotales = 0.0;

    public void ajouterAliment(Aliment a) {
        aliments.add(a);
        calculerCalories();
    }

    public Double calculerCalories() {
        caloriesTotales = aliments.stream()
                .mapToDouble(a -> a.getCalories() == null ? 0 : a.getCalories())
                .sum();
        return caloriesTotales;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(String utilisateurId) { this.utilisateurId = utilisateurId; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public TypeRepas getType() { return type; }
    public void setType(TypeRepas type) { this.type = type; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public List<Aliment> getAliments() { return aliments; }
    public void setAliments(List<Aliment> aliments) {
        this.aliments = aliments == null ? new ArrayList<>() : aliments;
        calculerCalories();
    }
    public Double getCaloriesTotales() { return caloriesTotales; }
}