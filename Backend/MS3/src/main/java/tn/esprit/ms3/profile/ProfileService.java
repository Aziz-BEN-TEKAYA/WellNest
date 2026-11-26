package tn.esprit.ms3.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.ms3.dto.ExportRecord;
import tn.esprit.ms3.dto.TrendReport;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileService {

    private final ProfileRepository repo;

    // =================== Profile ===================
    public Profile find(Long id) {
        return repo.findById(id).orElseThrow(() -> notFound("Profil", id));
    }

    public List<Profile> findAll() {
        return repo.findAll();
    }

    public Profile create(Profile p) {
        p.setId(null);
        return repo.save(p);
    }

    public Profile update(Long id, Profile in) {
        Profile p = find(id);
        p.setUserId(in.getUserId());
        p.setAntecedents(in.getAntecedents());
        return p;
    }

    public void delete(Long id) {
        repo.delete(find(id));
    }

    // =================== Allergy ===================
    public Allergy addAllergy(Long profileId, Allergy a) {
        Profile p = find(profileId);
        a.setId(null);
        a.setProfile(p);
        p.getAllergies().add(a);
        repo.flush();
        return a;
    }

    public List<Allergy> allergies(Long profileId) {
        return find(profileId).getAllergies();
    }

    public Allergy updateAllergy(Long id, Allergy in) {
        Profile p = repo.findByAllergies_Id(id).orElseThrow(() -> notFound("Allergie", id));
        Allergy a = p.getAllergies().stream().filter(x -> x.getId().equals(id)).findFirst().get();
        a.setName(in.getName());
        return a;
    }

    public void deleteAllergy(Long id) {
        Profile p = repo.findByAllergies_Id(id).orElseThrow(() -> notFound("Allergie", id));
        p.getAllergies().removeIf(x -> x.getId().equals(id));
    }

    // =================== Treatment ===================
    public Treatment addTreatment(Long profileId, Treatment t) {
        Profile p = find(profileId);
        t.setId(null);
        t.setProfile(p);
        p.getTreatments().add(t);
        repo.flush();
        return t;
    }

    public List<Treatment> treatments(Long profileId) {
        return find(profileId).getTreatments();
    }

    public Treatment updateTreatment(Long id, Treatment in) {
        Profile p = repo.findByTreatments_Id(id).orElseThrow(() -> notFound("Traitement", id));
        Treatment t = p.getTreatments().stream().filter(x -> x.getId().equals(id)).findFirst().get();
        t.setName(in.getName());
        t.setDosage(in.getDosage());
        t.setStartDate(in.getStartDate());
        t.setEndDate(in.getEndDate());
        return t;
    }

    public void deleteTreatment(Long id) {
        Profile p = repo.findByTreatments_Id(id).orElseThrow(() -> notFound("Traitement", id));
        p.getTreatments().removeIf(x -> x.getId().equals(id));
    }

    // =================== Appointment ===================
    public Appointment addAppointment(Long profileId, Appointment a) {
        Profile p = find(profileId);
        a.setId(null);
        a.setProfile(p);
        p.getAppointments().add(a);
        repo.flush();
        return a;
    }

    public List<Appointment> appointments(Long profileId) {
        return find(profileId).getAppointments();
    }

    public Appointment updateAppointment(Long id, Appointment in) {
        Profile p = repo.findByAppointments_Id(id).orElseThrow(() -> notFound("Rendez-vous", id));
        Appointment a = p.getAppointments().stream().filter(x -> x.getId().equals(id)).findFirst().get();
        a.setDateTime(in.getDateTime());
        a.setDoctorName(in.getDoctorName());
        a.setReason(in.getReason());
        if (in.getReminderSent() != null) a.setReminderSent(in.getReminderSent());
        return a;
    }

    public void deleteAppointment(Long id) {
        Profile p = repo.findByAppointments_Id(id).orElseThrow(() -> notFound("Rendez-vous", id));
        p.getAppointments().removeIf(x -> x.getId().equals(id));
    }

    // =================== Measurement (+ Alert) ===================
    public Measurement addMeasurement(Long profileId, Measurement m) {
        Profile p = find(profileId);
        m.setId(null);
        m.setProfile(p);
        if (m.getTimestamp() == null) m.setTimestamp(LocalDateTime.now());

        m.setStatus(evaluate(p, m));
        if (m.getStatus() == MeasurementStatus.TO_WATCH) {
            Alert alert = new Alert();
            alert.setMessage("Valeur inhabituelle pour " + m.getType() + " : " + m.getValue()
                    + (m.getUnit() != null ? " " + m.getUnit() : "")
                    + " (a surveiller, ce n'est pas un diagnostic)");
            alert.setDetectedAt(LocalDateTime.now());
            alert.setMeasurement(m);
            m.getAlerts().add(alert);
        }
        p.getMeasurements().add(m);
        repo.flush();
        return m;
    }

    public List<Measurement> measurements(Long profileId, MeasurementType type) {
        return find(profileId).getMeasurements().stream()
                .filter(m -> type == null || m.getType() == type)
                .sorted(Comparator.comparing(Measurement::getTimestamp).reversed())
                .toList();
    }

    public void deleteMeasurement(Long id) {
        Profile p = repo.findByMeasurements_Id(id).orElseThrow(() -> notFound("Mesure", id));
        p.getMeasurements().removeIf(x -> x.getId().equals(id));
    }

    public List<Alert> alerts(Long profileId) {
        return find(profileId).getMeasurements().stream()
                .flatMap(m -> m.getAlerts().stream())
                .sorted(Comparator.comparing(Alert::getDetectedAt).reversed())
                .toList();
    }

    // =================== TrendReport ===================
    public List<TrendReport> trends(Long profileId, MeasurementType type, int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return find(profileId).getMeasurements().stream()
                .filter(m -> !m.getTimestamp().isBefore(since))
                .filter(m -> type == null || m.getType() == type)
                .collect(Collectors.groupingBy(Measurement::getType))
                .entrySet().stream()
                .map(e -> {
                    List<Measurement> l = e.getValue().stream()
                            .sorted(Comparator.comparing(Measurement::getTimestamp)).toList();
                    double avg = l.stream().mapToDouble(Measurement::getValue).average().orElse(0);
                    double first = l.get(0).getValue();
                    double last = l.get(l.size() - 1).getValue();
                    double variation = first == 0 ? 0 : (last - first) / first * 100;
                    return new TrendReport(e.getKey(), round(avg), round(variation), days + " jours");
                })
                .toList();
    }

    // =================== Export pour MS7 ===================
    @Transactional(readOnly = true)
    public List<ExportRecord> exportForAi() {
        return repo.findAll().stream()
                .flatMap(p -> p.getMeasurements().stream()
                        .map(m -> new ExportRecord(p.getUserId(), m.getTimestamp(), m.getType().name(),
                                m.getValue(), m.getUnit(), m.getSource(), m.getQuality())))
                .toList();
    }

    // =================== Utilitaires ===================
    private MeasurementStatus evaluate(Profile p, Measurement m) {
        double v = m.getValue();
        boolean watch = switch (m.getType()) {
            case BLOOD_PRESSURE -> v > 14 || v < 9;
            case GLYCEMIA -> v > 1.00 || v < 0.70;
            case WEIGHT -> {
                Optional<Measurement> prev = p.getMeasurements().stream()
                        .filter(x -> x.getType() == MeasurementType.WEIGHT)
                        .max(Comparator.comparing(Measurement::getTimestamp));
                yield prev.isPresent() && prev.get().getValue() != 0
                        && Math.abs(v - prev.get().getValue()) / prev.get().getValue() > 0.05;
            }
        };
        return watch ? MeasurementStatus.TO_WATCH : MeasurementStatus.STABLE;
    }

    private double round(double x) {
        return Math.round(x * 100.0) / 100.0;
    }

    private ResponseStatusException notFound(String what, Long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, what + " " + id + " introuvable");
    }
}