package tn.esprit.ms2.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Tests d'integration des CRUD : seances, resumes, periodes sedentaires, objectifs, consignes, alertes. */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ActivityTrackingControllerTests {

    private static final String BASE = "/api/v1/activity";

    @Autowired
    private MockMvc mockMvc;

    // ---------- Seances ----------

    @Test
    void creeUneSeanceEtCalculeLaDuree() throws Exception {
        long typeId = firstId(BASE + "/activity-types?intensity=HIGH");
        String location = postJson(BASE + "/sessions", """
                {"userId": 1, "activityTypeId": %d, "startTime": "2025-06-10T09:30:00",
                 "endTime": "2025-06-10T10:10:00", "distanceMeters": 2500, "perceivedEffort": 6}
                """.formatted(typeId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.durationMinutes").value(40))
                .andExpect(jsonPath("$.activityTypeName").value("Course a pied"))
                .andExpect(jsonPath("$.categoryName").value("Marche"))
                .andExpect(jsonPath("$.source").value("MANUAL"))
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(get(BASE + "/sessions").param("userId", "1")
                        .param("from", "2025-06-10").param("to", "2025-06-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
        mockMvc.perform(get(BASE + "/sessions").param("userId", "1").param("from", "2025-06-11"))
                .andExpect(jsonPath("$", hasSize(0)));

        mockMvc.perform(delete(location)).andExpect(status().isNoContent());
        mockMvc.perform(get(location)).andExpect(status().isNotFound());
    }

    @Test
    void refuseUneSeanceSansFinNiDureeOuAvecFinAvantDebut() throws Exception {
        long typeId = firstId(BASE + "/activity-types");
        postJson(BASE + "/sessions", """
                {"userId": 1, "activityTypeId": %d, "startTime": "2025-06-10T09:30:00"}
                """.formatted(typeId))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.message", containsString("duree")));

        postJson(BASE + "/sessions", """
                {"userId": 1, "activityTypeId": %d, "startTime": "2025-06-10T09:30:00",
                 "endTime": "2025-06-10T09:00:00"}
                """.formatted(typeId))
                .andExpect(status().isUnprocessableContent());

        postJson(BASE + "/sessions", """
                {"userId": 1, "activityTypeId": 999999, "startTime": "2025-06-10T09:30:00", "durationMinutes": 20}
                """)
                .andExpect(status().isNotFound());
    }

    @Test
    void refuseDeSupprimerUnTypeDActiviteUtiliseParUneSeance() throws Exception {
        long typeId = firstId(BASE + "/activity-types?intensity=HIGH");
        postJson(BASE + "/sessions", """
                {"userId": 1, "activityTypeId": %d, "startTime": "2025-06-10T09:30:00", "durationMinutes": 20}
                """.formatted(typeId))
                .andExpect(status().isCreated());

        mockMvc.perform(delete(BASE + "/activity-types/" + typeId))
                .andExpect(status().isUnprocessableContent());
    }

    // ---------- Resumes quotidiens ----------

    @Test
    void unSeulResumeParUtilisateurEtParJour() throws Exception {
        String body = """
                {"userId": 7, "date": "2025-06-10", "steps": 4200, "activeMinutes": 35, "sedentaryMinutes": 540}
                """;
        String location = postJson(BASE + "/daily", body)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.steps").value(4200))
                .andReturn().getResponse().getHeader("Location");

        postJson(BASE + "/daily", body).andExpect(status().isConflict());

        mockMvc.perform(put(location).contentType(MediaType.APPLICATION_JSON).content("""
                        {"userId": 7, "date": "2025-06-10", "steps": 5100}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.steps").value(5100));

        mockMvc.perform(get(BASE + "/daily").param("userId", "7"))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void refuseUnResumeDePlusDe24Heures() throws Exception {
        postJson(BASE + "/daily", """
                {"userId": 7, "date": "2025-06-11", "steps": 100, "activeMinutes": 900, "sedentaryMinutes": 900}
                """)
                .andExpect(status().isUnprocessableContent());

        postJson(BASE + "/daily", """
                {"userId": 7, "date": "2025-06-11", "steps": -5}
                """)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.steps").exists());
    }

    // ---------- Periodes sedentaires ----------

    @Test
    void creeUnePeriodeSedentaire() throws Exception {
        postJson(BASE + "/sedentary-periods", """
                {"userId": 3, "startTime": "2025-06-10T13:00:00", "endTime": "2025-06-10T15:30:00"}
                """)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.durationMinutes").value(150));

        mockMvc.perform(get(BASE + "/sedentary-periods").param("userId", "3"))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    // ---------- Objectifs ----------

    @Test
    void gereLesObjectifsActifs() throws Exception {
        postJson(BASE + "/goals", """
                {"userId": 4, "goalType": "DAILY_STEPS", "targetValue": 5000, "startDate": "2025-06-01"}
                """)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.active").value(true));
        postJson(BASE + "/goals", """
                {"userId": 4, "goalType": "SESSIONS_PER_WEEK", "targetValue": 3,
                 "startDate": "2025-06-01", "active": false}
                """)
                .andExpect(status().isCreated());

        mockMvc.perform(get(BASE + "/goals").param("userId", "4"))
                .andExpect(jsonPath("$", hasSize(2)));
        mockMvc.perform(get(BASE + "/goals").param("userId", "4").param("activeOnly", "true"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].goalType").value("DAILY_STEPS"));
    }

    @Test
    void refuseUnObjectifQuiFinitAvantDeCommencer() throws Exception {
        postJson(BASE + "/goals", """
                {"userId": 4, "goalType": "DAILY_STEPS", "targetValue": 5000,
                 "startDate": "2025-06-10", "endDate": "2025-06-01"}
                """)
                .andExpect(status().isUnprocessableContent());
    }

    // ---------- Consignes ----------

    @Test
    void creeUneConsigneQuiExclutDesCategories() throws Exception {
        long aquatiqueId = idOfCategory("Activites aquatiques");
        String location = postJson(BASE + "/restrictions", """
                {"userId": 5, "maxIntensity": "MODERATE", "origin": "PROFESSIONAL",
                 "notes": "Pas de piscine", "excludedCategoryIds": [%d]}
                """.formatted(aquatiqueId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.excludedCategories", hasSize(1)))
                .andExpect(jsonPath("$.excludedCategories[0].name").value("Activites aquatiques"))
                .andReturn().getResponse().getHeader("Location");

        // La categorie exclue ne peut plus etre supprimee (les types sont d'abord retires)
        for (long typeId : ids(BASE + "/categories/" + aquatiqueId + "/activity-types")) {
            mockMvc.perform(delete(BASE + "/activity-types/" + typeId)).andExpect(status().isNoContent());
        }
        mockMvc.perform(delete(BASE + "/categories/" + aquatiqueId))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.message", containsString("consigne")));

        mockMvc.perform(put(location).contentType(MediaType.APPLICATION_JSON).content("""
                        {"userId": 5, "origin": "PERSONAL", "excludedCategoryIds": []}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.excludedCategories", hasSize(0)));
    }

    @Test
    void refuseUneConsigneAvecUneCategorieInconnue() throws Exception {
        postJson(BASE + "/restrictions", """
                {"userId": 5, "origin": "PERSONAL", "excludedCategoryIds": [999999]}
                """)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Categorie introuvable (id = 999999)"));
    }

    // ---------- Alertes ----------

    @Test
    void creeUneAlerteEtChangeSonEtat() throws Exception {
        String location = postJson(BASE + "/alerts", """
                {"userId": 6, "detectedOn": "2025-06-10", "alertType": "STEP_DROP",
                 "baselineValue": 5000, "observedValue": 3200}
                """)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.deviationPercent").value(-36.0))
                .andExpect(jsonPath("$.status").value("NEW"))
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(patch(location + "/status").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"ACKNOWLEDGED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACKNOWLEDGED"));

        mockMvc.perform(get(BASE + "/alerts").param("userId", "6").param("status", "NEW"))
                .andExpect(jsonPath("$", hasSize(0)));
        mockMvc.perform(get(BASE + "/alerts").param("userId", "6").param("status", "ACKNOWLEDGED"))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    // ---------- Outils ----------

    private ResultActions postJson(String url, String json) throws Exception {
        return mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json));
    }

    private long firstId(String url) throws Exception {
        return ids(url)[0];
    }

    /** Extrait les champs "id" de premier niveau d'une liste JSON. */
    private long[] ids(String url) throws Exception {
        String json = mockMvc.perform(get(url)).andReturn().getResponse().getContentAsString();
        return java.util.regex.Pattern.compile("\\{\"id\":(\\d+)").matcher(json).results()
                .mapToLong(match -> Long.parseLong(match.group(1)))
                .toArray();
    }

    private long idOfCategory(String name) throws Exception {
        String json = mockMvc.perform(get(BASE + "/categories")).andReturn().getResponse().getContentAsString();
        int nameIndex = json.indexOf("\"name\":\"" + name + "\"");
        int idStart = json.lastIndexOf("\"id\":", nameIndex) + 5;
        return Long.parseLong(json.substring(idStart, json.indexOf(',', idStart)));
    }
}
