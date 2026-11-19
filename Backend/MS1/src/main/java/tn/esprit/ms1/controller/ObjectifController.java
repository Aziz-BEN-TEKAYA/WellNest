package tn.esprit.ms1.controller;

import tn.esprit.ms1.entity.ObjectifNutritionnel;
import tn.esprit.ms1.service.ObjectifService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nutrition/goals")
public class ObjectifController {

    private final ObjectifService service;

    public ObjectifController(ObjectifService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ObjectifNutritionnel creer(@Valid @RequestBody ObjectifNutritionnel objectif) { return service.creer(objectif); }

    @GetMapping
    public List<ObjectifNutritionnel> lister(@RequestParam(required = false) String userId) { return service.lister(userId); }

    @GetMapping("/{id}")
    public ObjectifNutritionnel trouver(@PathVariable String id) { return service.trouver(id); }

    @PutMapping("/{id}")
    public ObjectifNutritionnel modifier(@PathVariable String id, @Valid @RequestBody ObjectifNutritionnel objectif) {
        return service.modifier(id, objectif);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable String id) { service.supprimer(id); }
}
