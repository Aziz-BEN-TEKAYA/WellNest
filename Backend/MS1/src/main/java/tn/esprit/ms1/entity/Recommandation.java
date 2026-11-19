package tn.esprit.ms1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Recommandation {

    @Id
    private String id = UUID.randomUUID().toString();

    private String utilisateurId;

    /** HYDRATATION ou NUTRITION. */
    private String categorie;

    @Column(length = 500)
    private String contenu;

    private LocalDate dateCreation = LocalDate.now();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(String utilisateurId) { this.utilisateurId = utilisateurId; }
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }
    public LocalDate getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDate dateCreation) { this.dateCreation = dateCreation; }
}