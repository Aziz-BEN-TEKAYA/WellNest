package tn.esprit.ms4.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import tn.esprit.ms4.entity.enums.MoodLevel;

import java.time.LocalDateTime;

/** Humeur declaree par le retraite. */
@Entity
@Table(name = "mood_entries", indexes = @Index(name = "idx_mood_user", columnList = "user_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoodEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "mood_level", nullable = false, length = 20)
    private MoodLevel moodLevel;

    /** Score de 1 a 5 derive de moodLevel. */
    @Column(name = "mood_score", nullable = false)
    private Integer moodScore;

    @Column(length = 1000)
    private String description;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
