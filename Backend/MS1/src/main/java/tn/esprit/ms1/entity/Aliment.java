package tn.esprit.ms1.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

@Embeddable
public class Aliment {

    @NotBlank
    private String nom;
    private Double quantite;
    private Double calories;

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public Double getQuantite() { return quantite; }
    public void setQuantite(Double quantite) { this.quantite = quantite; }
    public Double getCalories() { return calories; }
    public void setCalories(Double calories) { this.calories = calories; }
}