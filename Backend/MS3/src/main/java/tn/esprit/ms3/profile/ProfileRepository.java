package tn.esprit.ms3.profile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByAllergies_Id(Long allergyId);

    Optional<Profile> findByTreatments_Id(Long treatmentId);

    Optional<Profile> findByAppointments_Id(Long appointmentId);

    Optional<Profile> findByMeasurements_Id(Long measurementId);
}