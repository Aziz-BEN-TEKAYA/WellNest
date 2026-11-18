package tn.esprit.ms2.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.ms2.entity.enums.IntensityLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * Categorie d'activites (marche, equilibre, renforcement...).
 * Geree en base pour alimenter la liste deroulante du frontend.
 */
@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String name;

    @Column(length = 500)
    private String description;

    /** Nom de l'icone affichee dans l'interface. */
    @Column(length = 60)
    private String icon;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private IntensityLevel defaultIntensity;

    /** Category 1 -- 0..* ActivityType (regroupe). */
    @OneToMany(mappedBy = "category")
    @Builder.Default
    @ToString.Exclude
    private List<ActivityType> activityTypes = new ArrayList<>();
}
