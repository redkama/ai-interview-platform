package com.aimentor.domain.education.history.controller;

import com.aimentor.domain.education.history.entity.HistoryEra;
import com.aimentor.domain.education.history.entity.HistoryLesson;
import com.aimentor.domain.education.history.entity.HistoryQuiz;
import com.aimentor.domain.education.history.repository.HistoryLessonRepository;
import com.aimentor.domain.education.history.repository.HistoryQuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "jwt.secret-key=test-secret-key-test-secret-key-test-secret-key",
        "jwt.access-token-expiration-seconds=1800",
        "jwt.refresh-token-expiration-seconds=1209600"
})
@AutoConfigureMockMvc
class HistoryEducationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HistoryLessonRepository historyLessonRepository;

    @Autowired
    private HistoryQuizRepository historyQuizRepository;

    private Long lessonId;
    private Long firstQuizId;
    private Long secondQuizId;

    @BeforeEach
    void setUp() {
        historyQuizRepository.deleteAll();
        historyLessonRepository.deleteAll();

        HistoryLesson lesson = historyLessonRepository.save(HistoryLesson.builder()
                .title("Joseon Reforms")
                .era(HistoryEra.JOSEON)
                .content("Study major Joseon reform policies.")
                .orderNum(1)
                .build());
        lessonId = lesson.getId();

        firstQuizId = historyQuizRepository.save(HistoryQuiz.builder()
                .lesson(lesson)
                .question("Who established Joseon?")
                .options("[\"Taejo\",\"Sejong\",\"Yeongjo\"]")
                .answer("Taejo")
                .explanation("Joseon was founded by Taejo Yi Seong-gye.")
                .build()).getId();
        secondQuizId = historyQuizRepository.save(HistoryQuiz.builder()
                .lesson(lesson)
                .question("Which code system was organized in Joseon?")
                .options("[\"Gyeongguk Daejeon\",\"Samguk Sagi\",\"Hunminjeongeum\"]")
                .answer("Gyeongguk Daejeon")
                .explanation("Gyeongguk Daejeon became the state code.")
                .build()).getId();
    }

    @Test
    void historyEducationEndpointsShouldWorkWithOwnedHistory() throws Exception {
        String accessToken = signupAndGetAccessToken("history@example.com");
        String otherAccessToken = signupAndGetAccessToken("history-other@example.com");

        mockMvc.perform(get("/api/education/history/lessons")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(accessToken))
                        .param("era", "JOSEON"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].title").value("Joseon Reforms"));

        mockMvc.perform(get("/api/education/history/lessons/{id}", lessonId)
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.era").value("JOSEON"));

        mockMvc.perform(get("/api/education/history/lessons/{id}/quiz", lessonId)
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].options").value("[\"Taejo\",\"Sejong\",\"Yeongjo\"]"));

        mockMvc.perform(post("/api/education/history/explain")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(accessToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "topic": "Joseon Reforms",
                                  "era": "JOSEON"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.explanation").isNotEmpty())
                .andExpect(jsonPath("$.data.keyPoints.length()").value(3));

        mockMvc.perform(post("/api/education/history/quiz/submit")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(accessToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "lessonId": %d,
                                  "answers": [
                                    {
                                      "quizId": %d,
                                      "selectedAnswer": "Taejo"
                                    },
                                    {
                                      "quizId": %d,
                                      "selectedAnswer": "Hunminjeongeum"
                                    }
                                  ]
                                }
                                """.formatted(lessonId, firstQuizId, secondQuizId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.lessonId").value(lessonId))
                .andExpect(jsonPath("$.data.score").value(50))
                .andExpect(jsonPath("$.data.results.length()").value(2));

        mockMvc.perform(get("/api/education/history/my-history")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].lessonId").value(lessonId));

        mockMvc.perform(get("/api/education/history/my-history")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(otherAccessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(0));
    }

    @Test
    void historyWriteEndpointsShouldRequireAuthentication() throws Exception {
        mockMvc.perform(post("/api/education/history/explain")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "topic": "Joseon Reforms",
                                  "era": "JOSEON"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error.code").value("UNAUTHORIZED"));

        mockMvc.perform(post("/api/education/history/quiz/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "lessonId": 1,
                                  "answers": [
                                    {
                                      "quizId": 1,
                                      "selectedAnswer": "Taejo"
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error.code").value("UNAUTHORIZED"));
    }

    private String signupAndGetAccessToken(String email) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "History User",
                                  "email": "%s",
                                  "password": "password1"
                                }
                                """.formatted(email)))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        String accessToken = response.path("data").path("accessToken").asText();
        assertThat(accessToken).isNotBlank();
        return accessToken;
    }

    private String bearerToken(String accessToken) {
        return "Bearer " + accessToken;
    }
}
