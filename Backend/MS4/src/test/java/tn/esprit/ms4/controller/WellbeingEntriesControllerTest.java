package tn.esprit.ms4.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Humeur, stress, activites et rappels. */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WellbeingEntriesControllerTest {

    private static final String BASE = "/api/v1/sleep-wellness";

    @Autowired
    private MockMvc mvc;

    @Test
    void moodScoreIsDerivedFromLevel() throws Exception {
        mvc.perform(post(BASE + "/mood-entries").header("X-User-Id", 1).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"moodLevel\":\"GOOD\",\"description\":\"Belle promenade\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.moodScore").value(4))
                .andExpect(jsonPath("$.recordedAt").exists());
    }

    @Test
    void unknownMoodIsRejected() throws Exception {
        mvc.perform(post(BASE + "/mood-entries").header("X-User-Id", 1).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"moodLevel\":\"EXCITED\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void stressCrud() throws Exception {
        String location = mvc.perform(post(BASE + "/stress-entries").header("X-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stressLevel\":2,\"cause\":\"Rendez-vous medical\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
        String path = location.substring(location.indexOf(BASE));

        mvc.perform(put(path).header("X-User-Id", 1).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stressLevel\":4,\"cause\":\"Rendez-vous medical\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stressLevel").value(4));
        mvc.perform(delete(path).header("X-User-Id", 1)).andExpect(status().isNoContent());
    }

    @Test
    void stressLevelOutOfRangeIsRejected() throws Exception {
        mvc.perform(post(BASE + "/stress-entries").header("X-User-Id", 1).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stressLevel\":0}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void wellnessActivityCreateAndList() throws Exception {
        mvc.perform(post(BASE + "/wellness-activities").header("X-User-Id", 1).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"activityType\":\"BREATHING\",\"durationMinutes\":5,\"activityDate\":\"2026-01-13T21:00:00\"}"))
                .andExpect(status().isCreated());
        mvc.perform(get(BASE + "/wellness-activities").header("X-User-Id", 1).param("from", "2026-01-13").param("to", "2026-01-13"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].activityType").value("BREATHING"));
    }

    @Test
    void reminderDefaultsToEnabledAndRejectsDuplicates() throws Exception {
        String body = "{\"reminderType\":\"SLEEP_LOG\",\"reminderTime\":\"08:00:00\"}";
        mvc.perform(post(BASE + "/wellness-reminders").header("X-User-Id", 1).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.enabled").value(true));
        mvc.perform(post(BASE + "/wellness-reminders").header("X-User-Id", 1).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isConflict());
        mvc.perform(get(BASE + "/wellness-reminders").header("X-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
