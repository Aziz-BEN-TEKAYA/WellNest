package tn.esprit.ms1.controller;

import tn.esprit.ms1.dto.ObjectifNutritionnelRequest;
import tn.esprit.ms1.dto.ObjectifNutritionnelResponse;
import tn.esprit.ms1.mapper.ObjectifNutritionnelMapper;
import tn.esprit.ms1.service.ObjectifService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nutrition/goals")
public class ObjectifController {

    private final ObjectifService service;
    private final ObjectifNutritionnelMapper mapper;

    public ObjectifController(ObjectifService service, ObjectifNutritionnelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ObjectifNutritionnelResponse creer(@Valid @RequestBody ObjectifNutritionnelRequest request) {
        return mapper.toResponse(service.creer(mapper.toEntity(request)));
    }

    @GetMapping
    public List<ObjectifNutritionnelResponse> lister(@RequestParam(required = false) String userId) {
        return service.lister(userId).stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public ObjectifNutritionnelResponse trouver(@PathVariable String id) {
        return mapper.toResponse(service.trouver(id));
    }

    @PutMapping("/{id}")
    public ObjectifNutritionnelResponse modifier(@PathVariable String id,
                                                 @Valid @RequestBody ObjectifNutritionnelRequest request) {
        return mapper.toResponse(service.modifier(id, mapper.toEntity(request)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable String id) { service.supprimer(id); }
}
