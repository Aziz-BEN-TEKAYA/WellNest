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
import tn.esprit.ms2.dto.ActivityTypeRequest;
import tn.esprit.ms2.dto.ActivityTypeResponse;
import tn.esprit.ms2.entity.enums.IntensityLevel;
import tn.esprit.ms2.service.ActivityTypeAssignmentService;
import tn.esprit.ms2.service.ActivityTypeCommandService;
import tn.esprit.ms2.service.ActivityTypeQueryService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/activity/activity-types")
@RequiredArgsConstructor
@Tag(name = "Types d'activite", description = "Types d'activite rattaches a une categorie")
public class ActivityTypeController {

    private final ActivityTypeQueryService activityTypeQueryService;
    private final ActivityTypeCommandService activityTypeCommandService;
    private final ActivityTypeAssignmentService activityTypeAssignmentService;

    @GetMapping
    @Operation(summary = "Lister et filtrer les types d'activite",
            description = "Tous les filtres sont facultatifs et cumulables")
    public List<ActivityTypeResponse> search(
            @Parameter(description = "Identifiant de la categorie") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "Intensite") @RequestParam(required = false) IntensityLevel intensity,
            @Parameter(description = "Adapte aux seniors") @RequestParam(required = false) Boolean suitableForSeniors) {
        return activityTypeQueryService.search(categoryId, intensity, suitableForSeniors);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un type d'activite")
    @ApiResponse(responseCode = "404", description = "Type d'activite introuvable")
    public ActivityTypeResponse findById(@PathVariable Long id) {
        return activityTypeQueryService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Creer un type d'activite")
    @ApiResponse(responseCode = "201", description = "Type d'activite cree")
    @ApiResponse(responseCode = "404", description = "Categorie introuvable")
    @ApiResponse(responseCode = "409", description = "Nom deja utilise")
    public ResponseEntity<ActivityTypeResponse> create(@Valid @RequestBody ActivityTypeRequest request) {
        ActivityTypeResponse created = activityTypeCommandService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un type d'activite")
    @ApiResponse(responseCode = "404", description = "Type d'activite ou categorie introuvable")
    public ActivityTypeResponse update(@PathVariable Long id, @Valid @RequestBody ActivityTypeRequest request) {
        return activityTypeCommandService.update(id, request);
    }

    @PutMapping("/{id}/category/{categoryId}")
    @Operation(summary = "Affecter un type d'activite a une categorie",
            description = "Le type quitte son ancienne categorie (une seule categorie par type)")
    @ApiResponse(responseCode = "404", description = "Type d'activite ou categorie introuvable")
    public ActivityTypeResponse assignToCategory(@PathVariable Long id, @PathVariable Long categoryId) {
        return activityTypeAssignmentService.assignToCategory(id, categoryId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un type d'activite", description = "Refuse si des seances l'utilisent")
    @ApiResponse(responseCode = "204", description = "Type d'activite supprime")
    @ApiResponse(responseCode = "422", description = "Type d'activite encore utilise")
    public void delete(@PathVariable Long id) {
        activityTypeCommandService.delete(id);
    }
}
