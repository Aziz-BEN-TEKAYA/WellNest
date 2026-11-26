package tn.esprit.ms2.health;

import org.springframework.boot.health.contributor.AbstractHealthIndicator;
import org.springframework.boot.health.contributor.Health;
import org.springframework.stereotype.Component;
import tn.esprit.ms2.repository.ActivityTypeRepository;
import tn.esprit.ms2.repository.CategoryRepository;

/**
 * HealthIndicator personnalise : composant "activityReferential" de /actuator/health.
 * <p>
 * Le service est DOWN si le referentiel est vide : sans categorie ni type d'activite,
 * la liste deroulante du frontend est vide et aucune seance ne peut etre saisie.
 * Si la base ne repond pas, l'exception est convertie en DOWN par AbstractHealthIndicator.
 */
@Component
public class ActivityReferentialHealthIndicator extends AbstractHealthIndicator {

    private final CategoryRepository categoryRepository;
    private final ActivityTypeRepository activityTypeRepository;

    public ActivityReferentialHealthIndicator(CategoryRepository categoryRepository,
                                              ActivityTypeRepository activityTypeRepository) {
        super("Referentiel d'activites inaccessible");
        this.categoryRepository = categoryRepository;
        this.activityTypeRepository = activityTypeRepository;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        long categories = categoryRepository.count();
        long activityTypes = activityTypeRepository.count();
        long suitableForSeniors = activityTypeRepository.countBySuitableForSeniorsTrue();

        builder.withDetail("categories", categories)
                .withDetail("activityTypes", activityTypes)
                .withDetail("activityTypesSuitableForSeniors", suitableForSeniors);

        if (categories == 0) {
            builder.down().withDetail("reason", "Aucune categorie : la liste deroulante du frontend est vide");
        } else if (activityTypes == 0) {
            builder.down().withDetail("reason", "Aucun type d'activite : aucune seance ne peut etre saisie");
        } else {
            builder.up();
        }
    }
}
