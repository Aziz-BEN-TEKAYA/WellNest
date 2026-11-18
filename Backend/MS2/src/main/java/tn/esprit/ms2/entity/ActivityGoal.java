package tn.esprit.ms2.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.ms2.entity.enums.GoalType;

import java.time.LocalDate;

/** Objectif d'activite fixe pour un utilisateur. */
@Entity
@Table(name = "activity_goal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private GoalType goalType;

    /** Valeur cible (pas, minutes ou nombre de seances selon goalType). */
    private int targetValue;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Builder.Default
    private boolean active = true;
}
