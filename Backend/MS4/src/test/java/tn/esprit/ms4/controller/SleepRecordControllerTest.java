package tn.esprit.ms4.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SleepRecordControllerTest {

    private static final String URL = "/api/v1/sleep-wellness/sleep-records";

    @Autowired
    private MockMvc mvc;

    private static String night(String bedtime, String wakeup, int quality) {
        return """
                {"bedtime":"%s","wakeupTime":"%s","sleepQuality":%d,"nightWakings":1,"notes":"Reveil en forme"}
                """.formatted(bedtime, wakeup, quality);
    }

    @Test
    void createComputesDurationAndSleepDate() throws Exception {
        mvc.perform(post(URL).header("X-User-Id", 1).contentType(MediaType.APPLICATION_JSON)
                        .content(night("2026-01-12T22:30:00", "2026-01-13T06:30:00", 4)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.durationMinutes").value(480))
                .andExpect(jsonPath("$.sleepDate").value("2026-01-13"))
                .andExpect(jsonPath("$.nightWakings").value(1));
    }

    @Test
    void wakeupBeforeBedtimeIsRejected() throws Exception {
        mvc.perform(post(URL).header("X-User-Id", 1).contentType(MediaType.APPLICATION_JSON)
                        .content(night("2026-01-13T06:30:00", "2026-01-12T22:30:00", 4)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", containsString("apres l'heure du coucher")));
    }

    @Test
    void futureWakeupIsRejected() throws Exception {
        mvc.perform(post(URL).header("X-User-Id", 1).contentType(MediaType.APPLICATION_JSON)
                        .content(night("2099-01-12T22:30:00", "2099-01-13T06:30:00", 4)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void nightLongerThan16HoursIsRejected() throws Exception {
        mvc.perform(post(URL).header("X-User-Id", 1).contentType(MediaType.APPLICATION_JSON)
                        .content(night("2026-01-11T22:30:00", "2026-01-13T06:30:00", 4)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void qualityOutOfRangeIsRejected() throws Exception {
        mvc.perform(post(URL).header("X-User-Id", 1).contentType(MediaType.APPLICATION_JSON)
                        .content(night("2026-01-12T22:30:00", "2026-01-13T06:30:00", 7)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.sleepQuality").exists());
    }

    @Test
    void secondNightSameDateIsConflict() throws Exception {
        mvc.perform(post(URL).header("X-User-Id", 1).contentType(MediaType.APPLICATION_JSON)
                        .content(night("2026-01-12T22:30:00", "2026-01-13T06:30:00", 4)))
                .andExpect(status().isCreated());
        mvc.perform(post(URL).header("X-User-Id", 1).contentType(MediaType.APPLICATION_JSON)
                        .content(night("2026-01-12T23:30:00", "2026-01-13T07:00:00", 3)))
                .andExpect(status().isConflict());
    }

    @Test
    void missingUserHeaderIsRejected() throws Exception {
        mvc.perform(get(URL)).andExpect(status().isBadRequest());
    }

    @Test
    void fullCrudAndOwnership() throws Exception {
        String location = mvc.perform(post(URL).header("X-User-Id", 1).contentType(MediaType.APPLICATION_JSON)
                        .content(night("2026-01-12T22:30:00", "2026-01-13T06:30:00", 4)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
        String path = location.substring(location.indexOf(URL));

        // un autre utilisateur ne voit pas cette nuit
        mvc.perform(get(path).header("X-User-Id", 2)).andExpect(status().isNotFound());

        mvc.perform(get(URL).header("X-User-Id", 1).param("from", "2026-01-01").param("to", "2026-01-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));

        mvc.perform(put(path).header("X-User-Id", 1).contentType(MediaType.APPLICATION_JSON)
                        .content(night("2026-01-12T23:00:00", "2026-01-13T06:00:00", 2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.durationMinutes").value(420))
                .andExpect(jsonPath("$.sleepQuality").value(2));

        mvc.perform(delete(path).header("X-User-Id", 1)).andExpect(status().isNoContent());
        mvc.perform(get(path).header("X-User-Id", 1)).andExpect(status().isNotFound());
    }
}
