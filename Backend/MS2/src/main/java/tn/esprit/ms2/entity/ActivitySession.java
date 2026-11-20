package tn.esprit.ms2.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.ms2.entity.enums.DataSource;

import java.time.LocalDateTime;

/** Seance d'activite realisee par un utilisateur. */
@Entity
@Table(name = "activity_session",
        indexes = @Index(name = "idx_session_user_start", columnList = "userId, startTime"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivitySession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Identifiant de l'utilisateur (Keycloak / Health Profile), pas de cle etrangere entre services. */
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private int durationMinutes;

    private double distanceMeters;

    /** Effort ressenti (echelle de Borg simplifiee, 1 a 10). */
    private int perceivedEffort;

    @Column(length = 1000)
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DataSource source;

    /** ActivitySession * -- 1 ActivityType. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "activity_type_id", nullable = false)
    @ToString.Exclude
    private ActivityType activityType;

    /** Affecte cette seance a un type d'activite en gardant les deux cotes de l'association coherents. */
    public void assignToActivityType(ActivityType newType) {
        if (activityType == newType) {
            return;
        }
        if (activityType != null) {
            activityType.getSessions().remove(this);
        }
        activityType = newType;
        newType.getSessions().add(this);
    }

    /** Retire la seance de la liste de son type (avant suppression). */
    public void detachFromActivityType() {
        if (activityType != null) {
            activityType.getSessions().remove(this);
        }
    }
}
