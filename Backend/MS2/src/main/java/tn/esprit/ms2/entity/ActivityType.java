package tn.esprit.ms2.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.ms2.entity.enums.IntensityLevel;

import java.util.ArrayList;
import java.util.List;

/** Type d'activite (ex. marche lente, tai-chi) rattache a une categorie. */
@Entity
@Table(name = "activity_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IntensityLevel intensity;

    /** Equivalent metabolique (MET), sert a estimer la depense. */
    private double metValue;

    private boolean suitableForSeniors;

    @Column(length = 500)
    private String description;

    /** ActivityType 0..* -- 1 Category. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @ToString.Exclude
    private Category category;

    /** ActivityType 1 -- 0..* ActivitySession. */
    @OneToMany(mappedBy = "activityType")
    @Builder.Default
    @ToString.Exclude
    private List<ActivitySession> sessions = new ArrayList<>();

    /** Affecte ce type a une categorie en gardant les deux cotes de l'association coherents. */
    public void assignToCategory(Category newCategory) {
        if (category == newCategory) {
            return;
        }
        if (category != null) {
            category.getActivityTypes().remove(this);
        }
        category = newCategory;
        newCategory.getActivityTypes().add(this);
    }
}
