package tn.esprit.ms2.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Tests des fonctions d'affectation et de desaffectation. */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AssignmentControllerTests {

    private static final String BASE = "/api/v1/activity";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void affecteUnTypeDActiviteAUneAutreCategorie() throws Exception {
        long marcheId = idOfCategory("Marche");
        long equilibreId = idOfCategory("Equilibre");
        long courseId = firstId(BASE + "/activity-types?intensity=HIGH");

        mockMvc.perform(put(BASE + "/activity-types/" + courseId + "/category/" + equilibreId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(equilibreId))
                .andExpect(jsonPath("$.categoryName").value("Equilibre"));

        mockMvc.perform(get(BASE + "/categories/" + marcheId)).andExpect(jsonPath("$.activityTypeCount").value(3));
        mockMvc.perform(get(BASE + "/categories/" + equilibreId)).andExpect(jsonPath("$.activityTypeCount").value(3));

        mockMvc.perform(put(BASE + "/activity-types/" + courseId + "/category/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void affecteUneSeanceAUnAutreTypeDActivite() throws Exception {
        long[] typeIds = ids(BASE + "/activity-types");
        String location = mockMvc.perform(post(BASE + "/sessions").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"userId": 1, "activityTypeId": %d, "startTime": "2025-06-10T09:00:00",
                                 "durationMinutes": 30}
                                """.formatted(typeIds[0])))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(put(location + "/activity-type/" + typeIds[1]))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activityTypeId").value(typeIds[1]));

        // L'ancien type n'est plus utilise : il peut etre supprime ; le nouveau, non
        mockMvc.perform(delete(BASE + "/activity-types/" + typeIds[0])).andExpect(status().isNoContent());
        mockMvc.perform(delete(BASE + "/activity-types/" + typeIds[1])).andExpect(status().isUnprocessableContent());
    }

    @Test
    void affecteEtDesaffecteUneCategorieExclueDUneConsigne() throws Exception {
        long aquatiqueId = idOfCategory("Activites aquatiques");
        long marcheId = idOfCategory("Marche");
        String location = mockMvc.perform(post(BASE + "/restrictions").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"userId": 5, "origin": "PROFESSIONAL", "excludedCategoryIds": [%d]}
                                """.formatted(aquatiqueId)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        // Affectation
        mockMvc.perform(put(location + "/excluded-categories/" + marcheId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.excludedCategories", hasSize(2)));
        mockMvc.perform(put(location + "/excluded-categories/" + marcheId))
                .andExpect(status().isConflict());

        // Desaffectation
        mockMvc.perform(delete(location + "/excluded-categories/" + aquatiqueId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.excludedCategories", hasSize(1)))
                .andExpect(jsonPath("$.excludedCategories[0].name").value("Marche"));
        mockMvc.perform(delete(location + "/excluded-categories/" + aquatiqueId))
                .andExpect(status().isUnprocessableContent());

        mockMvc.perform(put(location + "/excluded-categories/999999")).andExpect(status().isNotFound());
        mockMvc.perform(put(BASE + "/restrictions/999999/excluded-categories/" + marcheId))
                .andExpect(status().isNotFound());
    }

    private long firstId(String url) throws Exception {
        return ids(url)[0];
    }

    private long[] ids(String url) throws Exception {
        String json = mockMvc.perform(get(url)).andReturn().getResponse().getContentAsString();
        return Pattern.compile("\\{\"id\":(\\d+)").matcher(json).results()
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
