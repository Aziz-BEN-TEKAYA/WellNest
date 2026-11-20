package tn.esprit.ms2.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.ms2.entity.enums.AlertStatus;
import tn.esprit.ms2.entity.enums.AlertType;

import java.time.LocalDate;

/**
 * Alerte de baisse de mobilite : valeur observee comparee a la reference
 * (mediane des pas sur 28 jours ; alerte sous 70 %).
 */
@Entity
@Table(name = "mobility_alert",
        indexes = @Index(name = "idx_alert_user_status", columnList = "userId, status"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MobilityAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDate detectedOn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AlertType alertType;

    private double baselineValue;

    private double observedValue;

    private double deviationPercent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private AlertStatus status = AlertStatus.NEW;
}
