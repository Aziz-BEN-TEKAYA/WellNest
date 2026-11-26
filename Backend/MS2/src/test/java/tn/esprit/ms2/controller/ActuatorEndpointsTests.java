package tn.esprit.ms2.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifie l'exposition et le contenu des endpoints Actuator (atelier Observabilite).
 * Le application.properties de test remplace celui de main (meme nom) : on charge donc
 * explicitement la vraie configuration (filtree par Maven), en gardant H2 et sans Eureka.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "file:target/classes/application.properties",
        properties = {
                "spring.datasource.url=jdbc:h2:mem:ms2_actuator;DB_CLOSE_DELAY=-1",
                "spring.datasource.username=sa",
                "spring.datasource.password=",
                "spring.jpa.hibernate.ddl-auto=create-drop",
                "spring.jpa.show-sql=false",
                "eureka.client.enabled=false"
        })
class ActuatorEndpointsTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void healthDetailleAvecLaBaseEtLeReferentiel() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.components.db.status").value("UP"))
                .andExpect(jsonPath("$.components.diskSpace.status").value("UP"))
                .andExpect(jsonPath("$.components.activityReferential.status").value("UP"))
                .andExpect(jsonPath("$.components.activityReferential.details.categories").value(6))
                .andExpect(jsonPath("$.components.activityReferential.details.activityTypes").value(14))
                .andExpect(jsonPath("$.components.activityReferential.details.activityTypesSuitableForSeniors")
                        .value(13));
    }

    @Test
    void infoDecritLeServiceEtLEquipe() throws Exception {
        mockMvc.perform(get("/actuator/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.app.name").value("WellNest - Physical Activity & Mobility"))
                .andExpect(jsonPath("$.app.version").value("0.0.1-SNAPSHOT"))
                .andExpect(jsonPath("$.app.team").value("WellNest"))
                .andExpect(jsonPath("$.java.version").exists());
    }

    @Test
    void metricsListeEtCompteLesRequetesHttp() throws Exception {
        mockMvc.perform(get("/api/v1/activity/categories")).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/activity/categories")).andExpect(status().isOk());

        mockMvc.perform(get("/actuator/metrics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.names", hasItems("jvm.memory.used", "http.server.requests")));

        mockMvc.perform(get("/actuator/metrics/jvm.memory.used"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.measurements[0].statistic").value("VALUE"));

        mockMvc.perform(get("/actuator/metrics/http.server.requests")
                        .param("tag", "uri:/api/v1/activity/categories")
                        .param("tag", "status:200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.measurements[?(@.statistic=='COUNT')].value",
                        hasItems(greaterThanOrEqualTo(2.0))));
    }

    @Test
    void loggersChangeLeNiveauSansRedemarrer() throws Exception {
        String logger = "/actuator/loggers/tn.esprit.ms2";
        mockMvc.perform(get(logger))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.effectiveLevel").value("INFO"));

        mockMvc.perform(post(logger).contentType(MediaType.APPLICATION_JSON).content("{\"configuredLevel\":\"DEBUG\"}"))
                .andExpect(status().isNoContent());
        mockMvc.perform(get(logger))
                .andExpect(jsonPath("$.configuredLevel").value("DEBUG"))
                .andExpect(jsonPath("$.effectiveLevel").value("DEBUG"));

        // Retour au niveau herite
        mockMvc.perform(post(logger).contentType(MediaType.APPLICATION_JSON).content("{\"configuredLevel\":null}"))
                .andExpect(status().isNoContent());
        mockMvc.perform(get(logger)).andExpect(jsonPath("$.effectiveLevel").value("INFO"));
    }

    @Test
    void lesEndpointsSensiblesNeSontPasExposes() throws Exception {
        mockMvc.perform(get("/actuator/env")).andExpect(status().isNotFound());
        mockMvc.perform(get("/actuator/beans")).andExpect(status().isNotFound());
    }
}
