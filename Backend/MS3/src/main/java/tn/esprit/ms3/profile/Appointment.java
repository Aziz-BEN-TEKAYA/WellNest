package tn.esprit.ms3.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime dateTime;

    private String doctorName;
    private String reason;
    private Boolean reminderSent = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Profile profile;
}