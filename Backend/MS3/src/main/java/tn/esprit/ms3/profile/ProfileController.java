package tn.esprit.ms3.profile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ms3.dto.ExportRecord;
import tn.esprit.ms3.dto.TrendReport;

import java.util.List;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService service;

    // ---------- Profil ----------
    @PostMapping("/profiles")
    @ResponseStatus(HttpStatus.CREATED)
    public Profile createProfile(@Valid @RequestBody Profile p) { return service.create(p); }

    @GetMapping("/profiles")
    public List<Profile> allProfiles() { return service.findAll(); }

    @GetMapping("/profiles/{id}")
    public Profile getProfile(@PathVariable Long id) { return service.find(id); }

    @PutMapping("/profiles/{id}")
    public Profile updateProfile(@PathVariable Long id, @Valid @RequestBody Profile p) { return service.update(id, p); }

    @DeleteMapping("/profiles/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@PathVariable Long id) { service.delete(id); }

    // ---------- Allergies ----------
    @PostMapping("/allergies")
    @ResponseStatus(HttpStatus.CREATED)
    public Allergy addAllergy(@RequestParam Long profileId, @Valid @RequestBody Allergy a) { return service.addAllergy(profileId, a); }

    @GetMapping("/allergies")
    public List<Allergy> allergies(@RequestParam Long profileId) { return service.allergies(profileId); }

    @PutMapping("/allergies/{id}")
    public Allergy updateAllergy(@PathVariable Long id, @Valid @RequestBody Allergy a) { return service.updateAllergy(id, a); }

    @DeleteMapping("/allergies/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllergy(@PathVariable Long id) { service.deleteAllergy(id); }

    // ---------- Traitements ----------
    @PostMapping("/treatments")
    @ResponseStatus(HttpStatus.CREATED)
    public Treatment addTreatment(@RequestParam Long profileId, @Valid @RequestBody Treatment t) { return service.addTreatment(profileId, t); }

    @GetMapping("/treatments")
    public List<Treatment> treatments(@RequestParam Long profileId) { return service.treatments(profileId); }

    @PutMapping("/treatments/{id}")
    public Treatment updateTreatment(@PathVariable Long id, @Valid @RequestBody Treatment t) { return service.updateTreatment(id, t); }

    @DeleteMapping("/treatments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTreatment(@PathVariable Long id) { service.deleteTreatment(id); }

    // ---------- Rendez-vous ----------
    @PostMapping("/appointments")
    @ResponseStatus(HttpStatus.CREATED)
    public Appointment addAppointment(@RequestParam Long profileId, @Valid @RequestBody Appointment a) { return service.addAppointment(profileId, a); }

    @GetMapping("/appointments")
    public List<Appointment> appointments(@RequestParam Long profileId) { return service.appointments(profileId); }

    @PutMapping("/appointments/{id}")
    public Appointment updateAppointment(@PathVariable Long id, @Valid @RequestBody Appointment a) { return service.updateAppointment(id, a); }

    @DeleteMapping("/appointments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAppointment(@PathVariable Long id) { service.deleteAppointment(id); }

    // ---------- Mesures ----------
    @PostMapping("/measurements")
    @ResponseStatus(HttpStatus.CREATED)
    public Measurement addMeasurement(@RequestParam Long profileId, @Valid @RequestBody Measurement m) { return service.addMeasurement(profileId, m); }

    @GetMapping("/measurements")
    public List<Measurement> measurements(@RequestParam Long profileId,
                                          @RequestParam(required = false) MeasurementType type) {
        return service.measurements(profileId, type);
    }

    @DeleteMapping("/measurements/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMeasurement(@PathVariable Long id) { service.deleteMeasurement(id); }

    // ---------- Tendances et alertes ----------
    @GetMapping("/measurements/trends")
    public List<TrendReport> trends(@RequestParam Long profileId,
                                    @RequestParam(required = false) MeasurementType type,
                                    @RequestParam(defaultValue = "30") int days) {
        return service.trends(profileId, type, days);
    }

    @GetMapping("/alerts")
    public List<Alert> alerts(@RequestParam Long profileId) { return service.alerts(profileId); }

    // ---------- Donnees lues par MS7 ----------
    @GetMapping("/export/ai")
    public List<ExportRecord> exportForAi() { return service.exportForAi(); }
}