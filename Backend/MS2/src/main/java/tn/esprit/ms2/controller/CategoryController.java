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
import tn.esprit.ms2.dto.ActivityTypeResponse;
import tn.esprit.ms2.dto.CategoryRequest;
import tn.esprit.ms2.dto.CategoryResponse;
import tn.esprit.ms2.service.ActivityTypeQueryService;
import tn.esprit.ms2.service.CategoryCommandService;
import tn.esprit.ms2.service.CategoryQueryService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/activity/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Categories d'activites (liste deroulante du frontend)")
public class CategoryController {

    private final CategoryQueryService categoryQueryService;
    private final CategoryCommandService categoryCommandService;
    private final ActivityTypeQueryService activityTypeQueryService;

    @GetMapping
    @Operation(summary = "Lister les categories", description = "Triees par nom, pour alimenter la liste deroulante")
    public List<CategoryResponse> findAll() {
        return categoryQueryService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter une categorie")
    @ApiResponse(responseCode = "404", description = "Categorie introuvable")
    public CategoryResponse findById(@PathVariable Long id) {
        return categoryQueryService.findById(id);
    }

    @GetMapping("/{id}/activity-types")
    @Operation(summary = "Lister les types d'activite d'une categorie")
    @ApiResponse(responseCode = "404", description = "Categorie introuvable")
    public List<ActivityTypeResponse> findActivityTypes(@PathVariable Long id) {
        return activityTypeQueryService.findByCategory(id);
    }

    @PostMapping
    @Operation(summary = "Creer une categorie")
    @ApiResponse(responseCode = "201", description = "Categorie creee")
    @ApiResponse(responseCode = "400", description = "Donnees invalides")
    @ApiResponse(responseCode = "409", description = "Nom deja utilise")
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse created = categoryCommandService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une categorie")
    @ApiResponse(responseCode = "404", description = "Categorie introuvable")
    @ApiResponse(responseCode = "409", description = "Nom deja utilise")
    public CategoryResponse update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        return categoryCommandService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer une categorie",
            description = "Refuse si des types d'activite y sont rattaches ou si une consigne l'exclut")
    @ApiResponse(responseCode = "204", description = "Categorie supprimee")
    @ApiResponse(responseCode = "422", description = "Categorie encore utilisee")
    public void delete(@PathVariable Long id) {
        categoryCommandService.delete(id);
    }
}
