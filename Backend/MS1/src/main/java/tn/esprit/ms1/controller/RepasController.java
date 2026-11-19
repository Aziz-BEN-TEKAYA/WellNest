package tn.esprit.ms1.controller;

import tn.esprit.ms1.entity.Repas;
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

    public RepasController(RepasService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Repas ajouter(@Valid @RequestBody Repas repas) { return service.ajouter(repas); }

    @GetMapping
    public List<Repas> lister(@RequestParam(required = false) String userId,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.lister(userId, date);
    }

    @GetMapping("/{id}")
    public Repas trouver(@PathVariable String id) { return service.trouver(id); }

    @PutMapping("/{id}")
    public Repas modifier(@PathVariable String id, @Valid @RequestBody Repas repas) { return service.modifier(id, repas); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable String id) { service.supprimer(id); }
}
