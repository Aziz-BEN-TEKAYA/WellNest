package tn.esprit.ms1.controller;

import tn.esprit.ms1.dto.SuiviHydratationRequest;
import tn.esprit.ms1.dto.SuiviHydratationResponse;
import tn.esprit.ms1.mapper.SuiviHydratationMapper;
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
    private final SuiviHydratationMapper mapper;

    public HydratationController(HydratationService service, SuiviHydratationMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuiviHydratationResponse enregistrer(@Valid @RequestBody SuiviHydratationRequest request) {
        return mapper.toResponse(service.enregistrerConsommation(mapper.toEntity(request)));
    }

    @GetMapping
    public List<SuiviHydratationResponse> lister(@RequestParam(required = false) String userId,
                                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.lister(userId, date).stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/summary")
    public Map<String, Object> bilan(@RequestParam String userId,
                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.bilan(userId, date);
    }
}
