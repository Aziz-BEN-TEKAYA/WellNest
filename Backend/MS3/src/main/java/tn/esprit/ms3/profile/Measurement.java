package tn.esprit.ms3.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MeasurementType type;

    @NotNull
    @Column(name = "measure_value")
    private Double value;

    private String unit;

    @Column(name = "measured_at")
    private LocalDateTime timestamp;

    private String source;
    private String quality;

    @Enumerated(EnumType.STRING)
    private MeasurementStatus status = MeasurementStatus.STABLE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Profile profile;

    @OneToMany(mappedBy = "measurement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alert> alerts = new ArrayList<>();
}