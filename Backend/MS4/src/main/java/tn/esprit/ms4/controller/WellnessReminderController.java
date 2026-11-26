package tn.esprit.ms4.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tn.esprit.ms4.dto.WellnessReminderRequest;
import tn.esprit.ms4.dto.WellnessReminderResponse;
import tn.esprit.ms4.service.WellnessReminderService;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.BASE_PATH + "/wellness-reminders")
@RequiredArgsConstructor
@Tag(name = "Wellness Reminders", description = "Rappels quotidiens de saisie (sommeil, humeur, stress, detente)")
public class WellnessReminderController {

    private final WellnessReminderService service;

    @PostMapping
    @Operation(summary = "Creer un rappel")
    public ResponseEntity<WellnessReminderResponse> create(@RequestHeader(ApiConstants.USER_HEADER) @Positive Long userId,
                                                           @Valid @RequestBody WellnessReminderRequest request) {
        WellnessReminderResponse created = service.create(userId, request);
        var location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    @Operation(summary = "Lister mes rappels")
    public List<WellnessReminderResponse> list(@RequestHeader(ApiConstants.USER_HEADER) @Positive Long userId) {
        return service.list(userId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un rappel")
    public WellnessReminderResponse getById(@RequestHeader(ApiConstants.USER_HEADER) @Positive Long userId,
                                            @PathVariable Long id) {
        return service.getById(userId, id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un rappel (heure, activation)")
    public WellnessReminderResponse update(@RequestHeader(ApiConstants.USER_HEADER) @Positive Long userId,
                                           @PathVariable Long id,
                                           @Valid @RequestBody WellnessReminderRequest request) {
        return service.update(userId, id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un rappel")
    public ResponseEntity<Void> delete(@RequestHeader(ApiConstants.USER_HEADER) @Positive Long userId,
                                       @PathVariable Long id) {
        service.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
}
