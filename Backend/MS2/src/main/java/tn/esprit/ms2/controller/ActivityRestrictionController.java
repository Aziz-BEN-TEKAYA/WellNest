package tn.esprit.ms2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tn.esprit.ms2.dto.ActivityRestrictionRequest;
import tn.esprit.ms2.dto.ActivityRestrictionResponse;
import tn.esprit.ms2.service.ActivityRestrictionCommandService;
import tn.esprit.ms2.service.ActivityRestrictionQueryService;
import tn.esprit.ms2.service.RestrictionCategoryAssignmentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/activity/restrictions")
@RequiredArgsConstructor
@Tag(name = "Consignes", description = "Restrictions d'activite (intensite maximale, categories exclues)")
public class ActivityRestrictionController {

    private final ActivityRestrictionQueryService restrictionQueryService;
    private final ActivityRestrictionCommandService restrictionCommandService;
    private final RestrictionCategoryAssignmentService categoryAssignmentService;

    @GetMapping
    @Operation(summary = "Lister les consignes d'un utilisateur")
    public List<ActivityRestrictionResponse> findByUser(@RequestParam Long userId) {
        return restrictionQueryService.findByUser(userId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter une consigne")
    @ApiResponse(responseCode = "404", description = "Consigne introuvable")
    public ActivityRestrictionResponse findById(@PathVariable Long id) {
        return restrictionQueryService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Creer une consigne", description = "Appele aussi par Health Profile (origin = PROFESSIONAL)")
    @ApiResponse(responseCode = "201", description = "Consigne creee")
    @ApiResponse(responseCode = "404", description = "Categorie exclue introuvable")
    public ResponseEntity<ActivityRestrictionResponse> create(@Valid @RequestBody ActivityRestrictionRequest request) {
        ActivityRestrictionResponse created = restrictionCommandService.create(request);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri()).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une consigne")
    @ApiResponse(responseCode = "404", description = "Consigne ou categorie introuvable")
    public ActivityRestrictionResponse update(@PathVariable Long id,
                                              @Valid @RequestBody ActivityRestrictionRequest request) {
        return restrictionCommandService.update(id, request);
    }

    @PutMapping("/{id}/excluded-categories/{categoryId}")
    @Operation(summary = "Affecter une categorie exclue a une consigne")
    @ApiResponse(responseCode = "404", description = "Consigne ou categorie introuvable")
    @ApiResponse(responseCode = "409", description = "Categorie deja exclue par cette consigne")
    public ActivityRestrictionResponse assignExcludedCategory(@PathVariable Long id, @PathVariable Long categoryId) {
        return categoryAssignmentService.assignExcludedCategory(id, categoryId);
    }

    @DeleteMapping("/{id}/excluded-categories/{categoryId}")
    @Operation(summary = "Desaffecter une categorie exclue d'une consigne")
    @ApiResponse(responseCode = "404", description = "Consigne ou categorie introuvable")
    @ApiResponse(responseCode = "422", description = "Categorie non exclue par cette consigne")
    public ActivityRestrictionResponse unassignExcludedCategory(@PathVariable Long id, @PathVariable Long categoryId) {
        return categoryAssignmentService.unassignExcludedCategory(id, categoryId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer une consigne")
    @ApiResponse(responseCode = "204", description = "Consigne supprimee")
    public void delete(@PathVariable Long id) {
        restrictionCommandService.delete(id);
    }
}
