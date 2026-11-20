package tn.esprit.ms2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tn.esprit.ms2.dto.SedentaryPeriodRequest;
import tn.esprit.ms2.dto.SedentaryPeriodResponse;
import tn.esprit.ms2.service.SedentaryPeriodCommandService;
import tn.esprit.ms2.service.SedentaryPeriodQueryService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/activity/sedentary-periods")
@RequiredArgsConstructor
@Tag(name = "Periodes sedentaires", description = "Periodes prolongees sans activite")
public class SedentaryPeriodController {

    private final SedentaryPeriodQueryService periodQueryService;
    private final SedentaryPeriodCommandService periodCommandService;

    @GetMapping
    @Operation(summary = "Lister les periodes sedentaires d'un utilisateur",
            description = "Periode facultative (dates incluses)")
    public List<SedentaryPeriodResponse> findByUser(
            @RequestParam Long userId,
            @Parameter(example = "2026-11-01") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(example = "2026-11-30") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return periodQueryService.findByUser(userId, from, to);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter une periode sedentaire")
    @ApiResponse(responseCode = "404", description = "Periode introuvable")
    public SedentaryPeriodResponse findById(@PathVariable Long id) {
        return periodQueryService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Enregistrer une periode sedentaire")
    @ApiResponse(responseCode = "201", description = "Periode enregistree")
    @ApiResponse(responseCode = "422", description = "Fin avant le debut, ou ni fin ni duree")
    public ResponseEntity<SedentaryPeriodResponse> create(@Valid @RequestBody SedentaryPeriodRequest request) {
        SedentaryPeriodResponse created = periodCommandService.create(request);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri()).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une periode sedentaire")
    @ApiResponse(responseCode = "404", description = "Periode introuvable")
    public SedentaryPeriodResponse update(@PathVariable Long id, @Valid @RequestBody SedentaryPeriodRequest request) {
        return periodCommandService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer une periode sedentaire")
    @ApiResponse(responseCode = "204", description = "Periode supprimee")
    public void delete(@PathVariable Long id) {
        periodCommandService.delete(id);
    }
}
