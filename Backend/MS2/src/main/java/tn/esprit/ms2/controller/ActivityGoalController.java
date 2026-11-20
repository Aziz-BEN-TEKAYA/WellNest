package tn.esprit.ms2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tn.esprit.ms2.dto.ActivityGoalRequest;
import tn.esprit.ms2.dto.ActivityGoalResponse;
import tn.esprit.ms2.service.ActivityGoalCommandService;
import tn.esprit.ms2.service.ActivityGoalQueryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/activity/goals")
@RequiredArgsConstructor
@Tag(name = "Objectifs", description = "Objectifs d'activite (pas, minutes actives, seances)")
public class ActivityGoalController {

    private final ActivityGoalQueryService goalQueryService;
    private final ActivityGoalCommandService goalCommandService;

    @GetMapping
    @Operation(summary = "Lister les objectifs d'un utilisateur")
    public List<ActivityGoalResponse> findByUser(
            @RequestParam Long userId,
            @Parameter(description = "Seulement les objectifs actifs")
            @RequestParam(defaultValue = "false") boolean activeOnly) {
        return goalQueryService.findByUser(userId, activeOnly);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un objectif")
    @ApiResponse(responseCode = "404", description = "Objectif introuvable")
    public ActivityGoalResponse findById(@PathVariable Long id) {
        return goalQueryService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Creer un objectif")
    @ApiResponse(responseCode = "201", description = "Objectif cree")
    @ApiResponse(responseCode = "422", description = "Date de fin avant la date de debut")
    public ResponseEntity<ActivityGoalResponse> create(@Valid @RequestBody ActivityGoalRequest request) {
        ActivityGoalResponse created = goalCommandService.create(request);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri()).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un objectif")
    @ApiResponse(responseCode = "404", description = "Objectif introuvable")
    public ActivityGoalResponse update(@PathVariable Long id, @Valid @RequestBody ActivityGoalRequest request) {
        return goalCommandService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un objectif")
    @ApiResponse(responseCode = "204", description = "Objectif supprime")
    public void delete(@PathVariable Long id) {
        goalCommandService.delete(id);
    }
}
