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
import tn.esprit.ms2.dto.ActivitySessionRequest;
import tn.esprit.ms2.dto.ActivitySessionResponse;
import tn.esprit.ms2.service.ActivitySessionAssignmentService;
import tn.esprit.ms2.service.ActivitySessionCommandService;
import tn.esprit.ms2.service.ActivitySessionQueryService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/activity/sessions")
@RequiredArgsConstructor
@Tag(name = "Seances", description = "Seances d'activite realisees par les utilisateurs")
public class ActivitySessionController {

    private final ActivitySessionQueryService sessionQueryService;
    private final ActivitySessionCommandService sessionCommandService;
    private final ActivitySessionAssignmentService sessionAssignmentService;

    @GetMapping
    @Operation(summary = "Lister les seances d'un utilisateur", description = "Periode facultative (dates incluses)")
    public List<ActivitySessionResponse> findByUser(
            @RequestParam Long userId,
            @Parameter(example = "2026-11-01") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(example = "2026-11-30") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return sessionQueryService.findByUser(userId, from, to);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter une seance")
    @ApiResponse(responseCode = "404", description = "Seance introuvable")
    public ActivitySessionResponse findById(@PathVariable Long id) {
        return sessionQueryService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Enregistrer une seance", description = "La duree est calculee si l'heure de fin est fournie")
    @ApiResponse(responseCode = "201", description = "Seance enregistree")
    @ApiResponse(responseCode = "404", description = "Type d'activite introuvable")
    @ApiResponse(responseCode = "422", description = "Fin avant le debut, ou ni fin ni duree")
    public ResponseEntity<ActivitySessionResponse> create(@Valid @RequestBody ActivitySessionRequest request) {
        ActivitySessionResponse created = sessionCommandService.create(request);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri()).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une seance")
    @ApiResponse(responseCode = "404", description = "Seance ou type d'activite introuvable")
    public ActivitySessionResponse update(@PathVariable Long id, @Valid @RequestBody ActivitySessionRequest request) {
        return sessionCommandService.update(id, request);
    }

    @PutMapping("/{id}/activity-type/{activityTypeId}")
    @Operation(summary = "Affecter une seance a un type d'activite",
            description = "La seance quitte son ancien type (un seul type par seance)")
    @ApiResponse(responseCode = "404", description = "Seance ou type d'activite introuvable")
    public ActivitySessionResponse assignToActivityType(@PathVariable Long id, @PathVariable Long activityTypeId) {
        return sessionAssignmentService.assignToActivityType(id, activityTypeId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer une seance")
    @ApiResponse(responseCode = "204", description = "Seance supprimee")
    public void delete(@PathVariable Long id) {
        sessionCommandService.delete(id);
    }
}
