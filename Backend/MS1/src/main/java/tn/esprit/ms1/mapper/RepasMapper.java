package tn.esprit.ms1.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.ms1.dto.AlimentDto;
import tn.esprit.ms1.dto.RepasRequest;
import tn.esprit.ms1.dto.RepasResponse;
import tn.esprit.ms1.entity.Aliment;
import tn.esprit.ms1.entity.Repas;

import java.util.ArrayList;
import java.util.List;

/** Conversions Repas <-> DTO. Le calcul des calories reste dans l'entite et le service. */
@Component
public class RepasMapper {

    public Repas toEntity(RepasRequest request) {
        Repas repas = new Repas();
        repas.setUtilisateurId(request.utilisateurId());
        repas.setNom(request.nom());
        repas.setType(request.type());
        repas.setDate(request.date());
        repas.setAliments(toAliments(request.aliments()));
        return repas;
    }

    public RepasResponse toResponse(Repas repas) {
        return new RepasResponse(
                repas.getId(),
                repas.getUtilisateurId(),
                repas.getNom(),
                repas.getType(),
                repas.getDate(),
                repas.getAliments().stream().map(this::toDto).toList(),
                repas.getCaloriesTotales());
    }

    private List<Aliment> toAliments(List<AlimentDto> dtos) {
        List<Aliment> aliments = new ArrayList<>();
        if (dtos != null) {
            dtos.forEach(dto -> aliments.add(toAliment(dto)));
        }
        return aliments;
    }

    private Aliment toAliment(AlimentDto dto) {
        Aliment aliment = new Aliment();
        aliment.setNom(dto.nom());
        aliment.setQuantite(dto.quantite());
        aliment.setCalories(dto.calories());
        return aliment;
    }

    private AlimentDto toDto(Aliment aliment) {
        return new AlimentDto(aliment.getNom(), aliment.getQuantite(), aliment.getCalories());
    }
}
