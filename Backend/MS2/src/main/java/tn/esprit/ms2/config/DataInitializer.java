package tn.esprit.ms2.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms2.entity.ActivityType;
import tn.esprit.ms2.entity.Category;
import tn.esprit.ms2.entity.enums.IntensityLevel;
import tn.esprit.ms2.repository.ActivityTypeRepository;
import tn.esprit.ms2.repository.CategoryRepository;

import static tn.esprit.ms2.entity.enums.IntensityLevel.HIGH;
import static tn.esprit.ms2.entity.enums.IntensityLevel.LOW;
import static tn.esprit.ms2.entity.enums.IntensityLevel.MODERATE;

/**
 * Insere les categories et types d'activite de depart si la base est vide.
 * Valeurs MET indicatives (Compendium of Physical Activities).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ActivityTypeRepository activityTypeRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (categoryRepository.count() > 0) {
            return;
        }

        Category marche = category("Marche", "Marches a l'exterieur ou en interieur", "walk", LOW);
        Category equilibre = category("Equilibre", "Exercices pour prevenir les chutes", "balance", LOW);
        Category renforcement = category("Renforcement musculaire", "Exercices avec poids leger ou elastique",
                "dumbbell", MODERATE);
        Category souplesse = category("Souplesse et etirements", "Mobilite articulaire et etirements doux",
                "stretch", LOW);
        Category aquatique = category("Activites aquatiques", "Activites en piscine, sans impact", "swim", MODERATE);
        Category quotidien = category("Activites du quotidien", "Jardinage, menage et autres taches actives",
                "home", LOW);

        type("Marche lente", LOW, 2.8, true, marche);
        type("Marche active", MODERATE, 3.5, true, marche);
        type("Marche nordique", MODERATE, 4.8, true, marche);
        type("Course a pied", HIGH, 8.0, false, marche);
        type("Tai-chi", LOW, 3.0, true, equilibre);
        type("Exercices d'equilibre", LOW, 2.3, true, equilibre);
        type("Gym douce sur chaise", LOW, 2.8, true, renforcement);
        type("Renforcement avec elastique", MODERATE, 3.5, true, renforcement);
        type("Yoga doux", LOW, 2.5, true, souplesse);
        type("Etirements", LOW, 2.3, true, souplesse);
        type("Aquagym", MODERATE, 5.3, true, aquatique);
        type("Natation lente", MODERATE, 5.8, true, aquatique);
        type("Jardinage", MODERATE, 3.8, true, quotidien);
        type("Taches menageres", LOW, 3.3, true, quotidien);

        log.info("Donnees initiales inserees : {} categories, {} types d'activite",
                categoryRepository.count(), activityTypeRepository.count());
    }

    private Category category(String name, String description, String icon, IntensityLevel defaultIntensity) {
        return categoryRepository.save(Category.builder()
                .name(name)
                .description(description)
                .icon(icon)
                .defaultIntensity(defaultIntensity)
                .build());
    }

    private void type(String name, IntensityLevel intensity, double met, boolean suitableForSeniors,
                      Category category) {
        ActivityType type = ActivityType.builder()
                .name(name)
                .intensity(intensity)
                .metValue(met)
                .suitableForSeniors(suitableForSeniors)
                .category(category)
                .build();
        category.getActivityTypes().add(type);
        activityTypeRepository.save(type);
    }
}
