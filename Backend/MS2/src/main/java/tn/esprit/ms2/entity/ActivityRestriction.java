package tn.esprit.ms2.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.ms2.entity.enums.IntensityLevel;
import tn.esprit.ms2.entity.enums.RestrictionOrigin;

import java.util.HashSet;
import java.util.Set;

/**
 * Consigne limitant l'activite d'un utilisateur (intensite maximale, categories exclues).
 * Peut etre envoyee par Health Profile (POST /restrictions).
 */
@Entity
@Table(name = "activity_restriction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityRestriction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private IntensityLevel maxIntensity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RestrictionOrigin origin;

    @Column(length = 1000)
    private String notes;

    /** ActivityRestriction 0..* -- 0..* Category (exclut). */
    @ManyToMany
    @JoinTable(name = "restriction_excluded_category",
            joinColumns = @JoinColumn(name = "restriction_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    @Builder.Default
    @ToString.Exclude
    private Set<Category> excludedCategories = new HashSet<>();

    /** @return false si la categorie etait deja exclue */
    public boolean excludeCategory(Category category) {
        return excludedCategories.add(category);
    }

    /** @return false si la categorie n'etait pas exclue */
    public boolean removeExcludedCategory(Category category) {
        return excludedCategories.remove(category);
    }
}
