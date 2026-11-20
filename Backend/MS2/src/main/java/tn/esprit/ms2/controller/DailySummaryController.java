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
import tn.esprit.ms2.dto.DailySummaryRequest;
import tn.esprit.ms2.dto.DailySummaryResponse;
import tn.esprit.ms2.service.DailySummaryCommandService;
import tn.esprit.ms2.service.DailySummaryQueryService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/activity/daily")
@RequiredArgsConstructor
@Tag(name = "Resumes quotidiens", description = "Pas, distance et minutes actives par jour")
public class DailySummaryController {

    private final DailySummaryQueryService summaryQueryService;
    private final DailySummaryCommandService summaryCommandService;

    @GetMapping
    @Operation(summary = "Lister les resumes d'un utilisateur", description = "Periode facultative (dates incluses)")
    public List<DailySummaryResponse> findByUser(
            @RequestParam Long userId,
            @Parameter(example = "2026-11-01") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(example = "2026-11-30") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return summaryQueryService.findByUser(userId, from, to);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un resume")
    @ApiResponse(responseCode = "404", description = "Resume introuvable")
    public DailySummaryResponse findById(@PathVariable Long id) {
        return summaryQueryService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Enregistrer le resume d'une journee")
    @ApiResponse(responseCode = "201", description = "Resume enregistre")
    @ApiResponse(responseCode = "409", description = "Un resume existe deja pour cet utilisateur a cette date")
    @ApiResponse(responseCode = "422", description = "Plus de 1440 minutes au total")
    public ResponseEntity<DailySummaryResponse> create(@Valid @RequestBody DailySummaryRequest request) {
        DailySummaryResponse created = summaryCommandService.create(request);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri()).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un resume")
    @ApiResponse(responseCode = "404", description = "Resume introuvable")
    @ApiResponse(responseCode = "409", description = "Un resume existe deja pour cet utilisateur a cette date")
    public DailySummaryResponse update(@PathVariable Long id, @Valid @RequestBody DailySummaryRequest request) {
        return summaryCommandService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un resume")
    @ApiResponse(responseCode = "204", description = "Resume supprime")
    public void delete(@PathVariable Long id) {
        summaryCommandService.delete(id);
    }
}
