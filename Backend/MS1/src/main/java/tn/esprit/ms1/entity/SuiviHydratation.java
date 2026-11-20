package tn.esprit.ms1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class SuiviHydratation {

    @Id
    private String id = UUID.randomUUID().toString();

    @NotBlank
    private String utilisateurId;

    @NotNull @Positive
    private Double quantiteEau;

    private LocalDate date = LocalDate.now();

    private String source = "MANUEL";

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(String utilisateurId) { this.utilisateurId = utilisateurId; }
    public Double getQuantiteEau() { return quantiteEau; }
    public void setQuantiteEau(Double quantiteEau) { this.quantiteEau = quantiteEau; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
}