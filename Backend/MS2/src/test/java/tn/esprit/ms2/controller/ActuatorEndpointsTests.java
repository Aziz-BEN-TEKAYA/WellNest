package tn.esprit.ms2.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifie l'exposition et le contenu des endpoints Actuator.
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
    void healthDetailleAvecLaBase() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.components.db.status").value("UP"))
                .andExpect(jsonPath("$.components.diskSpace.status").value("UP"));
    }

    @Test
    void infoDecritLeService() throws Exception {
        mockMvc.perform(get("/actuator/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.app.name").value("WellNest - Physical Activity & Mobility"))
                .andExpect(jsonPath("$.app.version").value("0.0.1-SNAPSHOT"))
                .andExpect(jsonPath("$.java.version").exists());
    }

    @Test
    void lesAutresEndpointsNeSontPasExposes() throws Exception {
        mockMvc.perform(get("/actuator/env")).andExpect(status().isNotFound());
        mockMvc.perform(get("/actuator/beans")).andExpect(status().isNotFound());
    }
}
