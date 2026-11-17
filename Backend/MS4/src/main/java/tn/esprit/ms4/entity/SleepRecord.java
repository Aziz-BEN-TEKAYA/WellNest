package tn.esprit.ms4.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Une nuit de sommeil saisie manuellement par le retraite.
 * La duree est calculee par le backend a partir des horaires.
 * Une seule nuit par utilisateur et par date de reveil.
 */
@Entity
@Table(name = "sleep_records",
        uniqueConstraints = @UniqueConstraint(name = "uk_sleep_user_date", columnNames = {"user_id", "sleep_date"}),
        indexes = @Index(name = "idx_sleep_user", columnList = "user_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SleepRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Identifiant externe (systeme d'identite WellNest), sans cle etrangere. */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** Date de la nuit = date du reveil. */
    @Column(name = "sleep_date", nullable = false)
    private LocalDate sleepDate;

    @Column(nullable = false)
    private LocalDateTime bedtime;

    @Column(name = "wakeup_time", nullable = false)
    private LocalDateTime wakeupTime;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    /** Qualite ressentie de 1 (tres mauvaise) a 5 (tres bonne). */
    @Column(name = "sleep_quality", nullable = false)
    private Integer sleepQuality;

    @Column(name = "night_wakings", nullable = false)
    private Integer nightWakings;

    @Column(length = 1000)
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
