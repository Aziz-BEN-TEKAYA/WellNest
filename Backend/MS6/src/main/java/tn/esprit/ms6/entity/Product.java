package tn.esprit.ms6.entity;

import jakarta.persistence.Column;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.ms6.enums.AutonomyNeed;
import tn.esprit.ms6.enums.Obstacle;
import tn.esprit.ms6.enums.UsageDuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 1000)
    private String description;

    /** Prix en dinars tunisiens (TND), 3 decimales (millimes). */
    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock;

    @Column(length = 500)
    private String image;

    // ----- Caracteristiques (comparateur et compatibilite) -----

    @Column(name = "weight_kg", precision = 6, scale = 2)
    private BigDecimal weightKg;

    @Column(name = "width_cm", precision = 6, scale = 1)
    private BigDecimal widthCm;

    @Column(name = "height_cm", precision = 6, scale = 1)
    private BigDecimal heightCm;

    @Column(name = "depth_cm", precision = 6, scale = 1)
    private BigDecimal depthCm;

    /** Largeur une fois plie (si le produit est pliable). */
    @Column(name = "folded_width_cm", precision = 6, scale = 1)
    private BigDecimal foldedWidthCm;

    /** Largeur minimale de passage necessaire (porte) pour utiliser ou faire entrer le produit. */
    @Column(name = "minimum_door_width_cm", precision = 6, scale = 1)
    private BigDecimal minimumDoorWidthCm;

    @Column(length = 100)
    private String material;

    @Column(name = "warranty_months")
    private Integer warrantyMonths;

    @Column(name = "max_load_kg")
    private Integer maxLoadKg;

    /**
     * Information pour le client : le produit doit etre pose par un professionnel.
     * La boutique n'assure PAS l'installation, elle se contente d'informer.
     */
    @Column(name = "installation_required", nullable = false)
    @Builder.Default
    private boolean installationRequired = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "usage_duration", nullable = false, length = 20)
    private UsageDuration usageDuration;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ----- Etiquettes pour la recherche par obstacle et par besoin -----

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_obstacle", joinColumns = @JoinColumn(name = "product_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "obstacle", nullable = false, length = 30)
    @Builder.Default
    private Set<Obstacle> obstacles = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_need", joinColumns = @JoinColumn(name = "product_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "need", nullable = false, length = 30)
    @Builder.Default
    private Set<AutonomyNeed> needs = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (usageDuration == null) {
            usageDuration = UsageDuration.BOTH;
        }
    }
}
