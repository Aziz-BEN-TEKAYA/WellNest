package tn.esprit.ms1.controller;

import tn.esprit.ms1.dto.RecommandationResponse;
import tn.esprit.ms1.mapper.RecommandationMapper;
import tn.esprit.ms1.service.RecommandationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nutrition/recommendations")
public class RecommandationController {

    private final RecommandationService service;
    private final RecommandationMapper mapper;

    public RecommandationController(RecommandationService service, RecommandationMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    /** Avec ?userId=... les recommandations du jour sont (re)générées puis retournées. */
    @GetMapping
    public List<RecommandationResponse> lister(@RequestParam(required = false) String userId) {
        return service.lister(userId).stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public RecommandationResponse trouver(@PathVariable String id) { return mapper.toResponse(service.trouver(id)); }
}
