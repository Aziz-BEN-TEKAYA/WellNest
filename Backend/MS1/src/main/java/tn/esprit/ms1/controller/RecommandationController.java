package tn.esprit.ms1.controller;

import tn.esprit.ms1.entity.Recommandation;
import tn.esprit.ms1.service.RecommandationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nutrition/recommendations")
public class RecommandationController {

    private final RecommandationService service;

    public RecommandationController(RecommandationService service) { this.service = service; }

    /** Avec ?userId=... les recommandations du jour sont (re)générées puis retournées. */
    @GetMapping
    public List<Recommandation> lister(@RequestParam(required = false) String userId) { return service.lister(userId); }

    @GetMapping("/{id}")
    public Recommandation trouver(@PathVariable String id) { return service.trouver(id); }
}
