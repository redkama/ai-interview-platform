package com.aimentor.domain.education.english.controller;

import com.aimentor.domain.education.english.entity.EnglishLesson;
import com.aimentor.domain.education.english.entity.EnglishLevel;
import com.aimentor.domain.education.english.repository.EnglishLessonRepository;
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
class EnglishEducationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EnglishLessonRepository englishLessonRepository;

    private Long lessonId;

    @BeforeEach
    void setUp() {
        englishLessonRepository.deleteAll();
        EnglishLesson savedLesson = englishLessonRepository.save(EnglishLesson.builder()
                .title("Basic Greetings")
                .part("Speaking")
                .level(EnglishLevel.BEGINNER)
                .content("Learn common greeting patterns.")
                .orderNum(1)
                .build());
        lessonId = savedLesson.getId();
    }

    @Test
    void englishEducationEndpointsShouldWorkForAuthenticatedUser() throws Exception {
        String accessToken = signupAndGetAccessToken("english@example.com");

        mockMvc.perform(get("/api/education/english/lessons")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(accessToken))
                        .param("level", "BEGINNER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].title").value("Basic Greetings"));

        mockMvc.perform(get("/api/education/english/lessons/{id}", lessonId)
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(lessonId))
                .andExpect(jsonPath("$.data.level").value("BEGINNER"));

        mockMvc.perform(post("/api/education/english/feedback")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(accessToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userInput": "I led a team project.",
                                  "expectedAnswer": "I led a team project.",
                                  "level": "BEGINNER"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.correct").value(true))
                .andExpect(jsonPath("$.data.provider").exists());

        mockMvc.perform(post("/api/education/english/level-test")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(accessToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "testScore": 82
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.level").value("ADVANCED"))
                .andExpect(jsonPath("$.data.testScore").value(82));

        mockMvc.perform(post("/api/education/english/history")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(accessToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "lessonId": %d,
                                  "score": 91
                                }
                                """.formatted(lessonId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.lessonId").value(lessonId))
                .andExpect(jsonPath("$.data.score").value(91));
    }

    @Test
    void englishWriteEndpointsShouldRequireAuthentication() throws Exception {
        mockMvc.perform(post("/api/education/english/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userInput": "test",
                                  "expectedAnswer": "test",
                                  "level": "BEGINNER"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error.code").value("UNAUTHORIZED"));

        mockMvc.perform(post("/api/education/english/level-test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "testScore": 40
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
                                  "name": "English User",
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
