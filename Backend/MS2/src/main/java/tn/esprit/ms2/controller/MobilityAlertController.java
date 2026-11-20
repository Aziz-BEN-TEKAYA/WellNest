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
import tn.esprit.ms2.dto.AlertStatusRequest;
import tn.esprit.ms2.dto.MobilityAlertRequest;
import tn.esprit.ms2.dto.MobilityAlertResponse;
import tn.esprit.ms2.entity.enums.AlertStatus;
import tn.esprit.ms2.service.MobilityAlertCommandService;
import tn.esprit.ms2.service.MobilityAlertQueryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/activity/alerts")
@RequiredArgsConstructor
@Tag(name = "Alertes de mobilite", description = "Alertes de baisse de mobilite")
public class MobilityAlertController {

    private final MobilityAlertQueryService alertQueryService;
    private final MobilityAlertCommandService alertCommandService;

    @GetMapping
    @Operation(summary = "Lister les alertes d'un utilisateur", description = "De la plus recente a la plus ancienne")
    public List<MobilityAlertResponse> findByUser(
            @RequestParam Long userId,
            @Parameter(description = "Filtrer par etat") @RequestParam(required = false) AlertStatus status) {
        return alertQueryService.findByUser(userId, status);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter une alerte")
    @ApiResponse(responseCode = "404", description = "Alerte introuvable")
    public MobilityAlertResponse findById(@PathVariable Long id) {
        return alertQueryService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Creer une alerte", description = "L'ecart en % est calcule a partir des deux valeurs")
    @ApiResponse(responseCode = "201", description = "Alerte creee")
    public ResponseEntity<MobilityAlertResponse> create(@Valid @RequestBody MobilityAlertRequest request) {
        MobilityAlertResponse created = alertCommandService.create(request);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri()).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une alerte")
    @ApiResponse(responseCode = "404", description = "Alerte introuvable")
    public MobilityAlertResponse update(@PathVariable Long id, @Valid @RequestBody MobilityAlertRequest request) {
        return alertCommandService.update(id, request);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Changer l'etat d'une alerte", description = "ACKNOWLEDGED (prise en compte) ou DISMISSED")
    @ApiResponse(responseCode = "404", description = "Alerte introuvable")
    public MobilityAlertResponse changeStatus(@PathVariable Long id, @Valid @RequestBody AlertStatusRequest request) {
        return alertCommandService.changeStatus(id, request.status());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer une alerte")
    @ApiResponse(responseCode = "204", description = "Alerte supprimee")
    public void delete(@PathVariable Long id) {
        alertCommandService.delete(id);
    }
}
