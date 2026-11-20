package tn.esprit.ms2.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.ms2.entity.enums.DataSource;

import java.time.LocalDate;

/** Resume quotidien de l'activite d'un utilisateur (une ligne par utilisateur et par jour). */
@Entity
@Table(name = "daily_activity_summary",
        uniqueConstraints = @UniqueConstraint(name = "uk_summary_user_date", columnNames = {"userId", "date"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyActivitySummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    private int steps;

    private double distanceMeters;

    private int activeMinutes;

    private int sedentaryMinutes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DataSource source;
}
