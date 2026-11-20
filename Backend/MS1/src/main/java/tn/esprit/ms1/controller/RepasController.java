package tn.esprit.ms1.controller;

import tn.esprit.ms1.dto.RepasRequest;
import tn.esprit.ms1.dto.RepasResponse;
import tn.esprit.ms1.mapper.RepasMapper;
import tn.esprit.ms1.service.RepasService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/nutrition/meals")
public class RepasController {

    private final RepasService service;
    private final RepasMapper mapper;

    public RepasController(RepasService service, RepasMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RepasResponse ajouter(@Valid @RequestBody RepasRequest request) {
        return mapper.toResponse(service.ajouter(mapper.toEntity(request)));
    }

    @GetMapping
    public List<RepasResponse> lister(@RequestParam(required = false) String userId,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.lister(userId, date).stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public RepasResponse trouver(@PathVariable String id) { return mapper.toResponse(service.trouver(id)); }

    @PutMapping("/{id}")
    public RepasResponse modifier(@PathVariable String id, @Valid @RequestBody RepasRequest request) {
        return mapper.toResponse(service.modifier(id, mapper.toEntity(request)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable String id) { service.supprimer(id); }
}
