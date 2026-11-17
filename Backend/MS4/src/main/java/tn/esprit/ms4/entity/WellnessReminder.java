package tn.esprit.ms4.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import tn.esprit.ms4.entity.enums.ReminderType;

import java.time.LocalDateTime;
import java.time.LocalTime;

/** Rappel quotidien de saisie (ex. "Comment avez-vous dormi ?" chaque matin a 8 h). */
@Entity
@Table(name = "wellness_reminders",
        uniqueConstraints = @UniqueConstraint(name = "uk_reminder_user_type_time",
                columnNames = {"user_id", "reminder_type", "reminder_time"}),
        indexes = @Index(name = "idx_reminder_user", columnList = "user_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WellnessReminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "reminder_type", nullable = false, length = 20)
    private ReminderType reminderType;

    /** Heure du rappel, repetee chaque jour. */
    @Column(name = "reminder_time", nullable = false)
    private LocalTime reminderTime;

    @Column(nullable = false)
    private Boolean enabled;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
