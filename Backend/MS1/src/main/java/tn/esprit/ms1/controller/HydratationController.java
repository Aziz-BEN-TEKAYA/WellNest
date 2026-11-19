package tn.esprit.ms1.controller;

import tn.esprit.ms1.entity.SuiviHydratation;
import tn.esprit.ms1.service.HydratationService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nutrition/water")
public class HydratationController {

    private final HydratationService service;

    public HydratationController(HydratationService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuiviHydratation enregistrer(@Valid @RequestBody SuiviHydratation suivi) {
        return service.enregistrerConsommation(suivi);
    }

    @GetMapping
    public List<SuiviHydratation> lister(@RequestParam(required = false) String userId,
                                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.lister(userId, date);
    }

    @GetMapping("/summary")
    public Map<String, Object> bilan(@RequestParam String userId,
                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.bilan(userId, date);
    }
}
