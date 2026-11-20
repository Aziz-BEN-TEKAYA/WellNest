package tn.esprit.ms2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "sedentary_period",
        indexes = @Index(name = "idx_sedentary_user_start", columnList = "userId, startTime"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SedentaryPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private int durationMinutes;
}
