package tn.esprit.ms2.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'integration (H2 + donnees initiales de DataInitializer).
 * Chaque test est annule a la fin (@Transactional).
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CategoryAndActivityTypeControllerTests {

    private static final String CATEGORIES = "/api/v1/activity/categories";
    private static final String TYPES = "/api/v1/activity/activity-types";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listeLesCategoriesInitialesTrieesParNom() throws Exception {
        mockMvc.perform(get(CATEGORIES))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$[0].name").value("Activites aquatiques"))
                .andExpect(jsonPath("$[0].activityTypeCount").value(2));
    }

    @Test
    void filtreLesTypesParIntensiteEtAdaptationSeniors() throws Exception {
        mockMvc.perform(get(TYPES).param("intensity", "HIGH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Course a pied"));

        mockMvc.perform(get(TYPES).param("suitableForSeniors", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(13)));
    }

    @Test
    void listeLesTypesDUneCategorie() throws Exception {
        long marcheId = idOfCategory("Marche");
        mockMvc.perform(get(CATEGORIES + "/" + marcheId + "/activity-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));

        mockMvc.perform(get(TYPES).param("categoryId", String.valueOf(marcheId)).param("intensity", "MODERATE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void creeModifieEtSupprimeUneCategorie() throws Exception {
        String location = mockMvc.perform(post(CATEGORIES).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Danse", "icon": "music", "defaultIntensity": "MODERATE"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString(CATEGORIES + "/")))
                .andExpect(jsonPath("$.name").value("Danse"))
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(put(location).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Danse de salon", "defaultIntensity": "LOW"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Danse de salon"))
                .andExpect(jsonPath("$.defaultIntensity").value("LOW"));

        mockMvc.perform(delete(location)).andExpect(status().isNoContent());
        mockMvc.perform(get(location)).andExpect(status().isNotFound());
    }

    @Test
    void refuseUnNomDejaUtiliseSansTenirCompteDeLaCasse() throws Exception {
        mockMvc.perform(post(CATEGORIES).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "  MARCHE "}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void renvoieLesErreursDeValidationParChamp() throws Exception {
        mockMvc.perform(post(CATEGORIES).contentType(MediaType.APPLICATION_JSON).content("{\"name\": \"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.name").value("Le nom est obligatoire"));

        mockMvc.perform(post(TYPES).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Velo", "intensity": "EXTREME", "metValue": 4, "categoryId": 1}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void renvoie404PourUneRessourceInconnue() throws Exception {
        mockMvc.perform(get(CATEGORIES + "/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Categorie introuvable (id = 999999)"));

        mockMvc.perform(post(TYPES).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Velo d'appartement", "intensity": "LOW", "metValue": 3.5, "categoryId": 999999}
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void refuseDeSupprimerUneCategorieQuiContientDesTypes() throws Exception {
        long marcheId = idOfCategory("Marche");
        mockMvc.perform(delete(CATEGORIES + "/" + marcheId))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.message", containsString("types d'activite")));
    }

    @Test
    void creeUnTypeDActiviteDansUneCategorie() throws Exception {
        long souplesseId = idOfCategory("Souplesse et etirements");
        mockMvc.perform(post(TYPES).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Pilates doux", "intensity": "LOW", "metValue": 3.0,
                                 "suitableForSeniors": true, "categoryId": %d}
                                """.formatted(souplesseId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryName").value("Souplesse et etirements"));

        mockMvc.perform(get(CATEGORIES + "/" + souplesseId))
                .andExpect(jsonPath("$.activityTypeCount").value(3));
    }

    @Test
    void genereLaDocumentationOpenApi() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("WellNest - Physical Activity & Mobility API"))
                .andExpect(jsonPath("$.paths['/api/v1/activity/categories']").exists())
                .andExpect(jsonPath("$.paths['/api/v1/activity/activity-types']").exists());
    }

    private long idOfCategory(String name) throws Exception {
        String json = mockMvc.perform(get(CATEGORIES)).andReturn().getResponse().getContentAsString();
        int nameIndex = json.indexOf("\"name\":\"" + name + "\"");
        int idStart = json.lastIndexOf("\"id\":", nameIndex) + 5;
        return Long.parseLong(json.substring(idStart, json.indexOf(',', idStart)));
    }
}
