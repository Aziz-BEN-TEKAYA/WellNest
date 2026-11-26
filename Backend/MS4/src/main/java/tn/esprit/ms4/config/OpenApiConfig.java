package tn.esprit.ms4.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ms4OpenApi() {
        return new OpenAPI().info(new Info()
                .title("WellNest MS4 - Sleep & Mental Wellness")
                .version("v1")
                .description("Suivi du sommeil, de l'humeur, du stress et des activites de detente. "
                        + "Toutes les donnees sont saisies manuellement par le retraite (pas de montre connectee). "
                        + "En attendant Keycloak, l'utilisateur est identifie par l'en-tete X-User-Id."));
    }
}
