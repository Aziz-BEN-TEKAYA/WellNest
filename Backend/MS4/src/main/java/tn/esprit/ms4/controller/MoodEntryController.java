package tn.esprit.ms4.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tn.esprit.ms4.dto.MoodEntryRequest;
import tn.esprit.ms4.dto.MoodEntryResponse;
import tn.esprit.ms4.dto.PageResponse;
import tn.esprit.ms4.service.MoodEntryService;

import java.time.LocalDate;

@RestController
@RequestMapping(ApiConstants.BASE_PATH + "/mood-entries")
@RequiredArgsConstructor
@Tag(name = "Mood Entries", description = "Humeur declaree")
public class MoodEntryController {

    private final MoodEntryService service;

    @PostMapping
    @Operation(summary = "Enregistrer")
    public ResponseEntity<MoodEntryResponse> create(@RequestHeader(ApiConstants.USER_HEADER) @Positive Long userId,
                                                @Valid @RequestBody MoodEntryRequest request) {
        MoodEntryResponse created = service.create(userId, request);
        var location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    @Operation(summary = "Consulter l'historique (filtre par periode, pagine)")
    public PageResponse<MoodEntryResponse> list(@RequestHeader(ApiConstants.USER_HEADER) @Positive Long userId,
                                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                                            @RequestParam(defaultValue = "0") @Min(0) int page,
                                            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return service.list(userId, from, to, page, size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter une saisie")
    public MoodEntryResponse getById(@RequestHeader(ApiConstants.USER_HEADER) @Positive Long userId,
                                 @PathVariable Long id) {
        return service.getById(userId, id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une saisie")
    public MoodEntryResponse update(@RequestHeader(ApiConstants.USER_HEADER) @Positive Long userId,
                                @PathVariable Long id,
                                @Valid @RequestBody MoodEntryRequest request) {
        return service.update(userId, id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une saisie")
    public ResponseEntity<Void> delete(@RequestHeader(ApiConstants.USER_HEADER) @Positive Long userId,
                                       @PathVariable Long id) {
        service.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
}
