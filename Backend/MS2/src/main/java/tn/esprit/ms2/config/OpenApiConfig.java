package tn.esprit.ms2.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Documentation Swagger : http://localhost:8082/swagger-ui.html */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI physicalActivityOpenApi() {
        return new OpenAPI().info(new Info()
                .title("WellNest - Physical Activity & Mobility API")
                .version("v1")
                .description("Suivi de l'activite physique et de la mobilite des seniors : "
                        + "categories, types d'activite, seances, resumes quotidiens, objectifs, "
                        + "consignes et alertes de baisse de mobilite."));
    }
}
