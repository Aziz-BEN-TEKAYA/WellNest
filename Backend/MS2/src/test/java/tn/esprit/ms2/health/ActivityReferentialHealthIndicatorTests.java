package tn.esprit.ms2.health;

import org.junit.jupiter.api.Test;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.Status;
import tn.esprit.ms2.repository.ActivityTypeRepository;
import tn.esprit.ms2.repository.CategoryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** Tests unitaires du HealthIndicator personnalise (repositories simules avec Mockito). */
class ActivityReferentialHealthIndicatorTests {

    private final CategoryRepository categoryRepository = mock(CategoryRepository.class);
    private final ActivityTypeRepository activityTypeRepository = mock(ActivityTypeRepository.class);
    private final ActivityReferentialHealthIndicator indicator =
            new ActivityReferentialHealthIndicator(categoryRepository, activityTypeRepository);

    @Test
    void upQuandLeReferentielEstRempli() {
        when(categoryRepository.count()).thenReturn(6L);
        when(activityTypeRepository.count()).thenReturn(14L);
        when(activityTypeRepository.countBySuitableForSeniorsTrue()).thenReturn(13L);

        Health health = indicator.health();

        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails()).containsEntry("categories", 6L).containsEntry("activityTypes", 14L);
    }

    @Test
    void downSansCategorie() {
        when(categoryRepository.count()).thenReturn(0L);

        Health health = indicator.health();

        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(health.getDetails()).containsKey("reason");
    }

    @Test
    void downSansTypeDActivite() {
        when(categoryRepository.count()).thenReturn(6L);
        when(activityTypeRepository.count()).thenReturn(0L);

        assertThat(indicator.health().getStatus()).isEqualTo(Status.DOWN);
    }

    @Test
    void downSiLaBaseNeRepondPas() {
        when(categoryRepository.count()).thenThrow(new IllegalStateException("Connexion refusee"));

        Health health = indicator.health();

        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(health.getDetails()).containsKey("error");
    }
}
