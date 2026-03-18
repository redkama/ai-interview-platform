package com.aimentor.domain.education.english.controller;

import com.aimentor.common.api.ApiResponse;
import com.aimentor.common.security.AuthenticatedUser;
import com.aimentor.domain.education.english.dto.request.EnglishLearningHistoryCreateRequest;
import com.aimentor.domain.education.english.dto.request.EnglishLevelTestRequest;
import com.aimentor.domain.education.english.dto.response.EnglishLearningHistoryResponse;
import com.aimentor.domain.education.english.dto.response.EnglishLessonResponse;
import com.aimentor.domain.education.english.dto.response.LevelTestResultResponse;
import com.aimentor.domain.education.english.entity.EnglishLevel;
import com.aimentor.domain.education.english.service.EnglishEducationService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/education/english")
public class EnglishEducationController {

    private final EnglishEducationService englishEducationService;

    public EnglishEducationController(EnglishEducationService englishEducationService) {
        this.englishEducationService = englishEducationService;
    }

    @GetMapping("/lessons")
    public ApiResponse<List<EnglishLessonResponse>> getLessons(@RequestParam EnglishLevel level) {
        return ApiResponse.success(englishEducationService.getLessons(level));
    }

    @GetMapping("/lessons/{id}")
    public ApiResponse<EnglishLessonResponse> getLesson(@PathVariable Long id) {
        return ApiResponse.success(englishEducationService.getLesson(id));
    }

    @PostMapping("/level-test")
    public ApiResponse<LevelTestResultResponse> createLevelTestResult(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody EnglishLevelTestRequest request
    ) {
        return ApiResponse.success(englishEducationService.createLevelTestResult(authenticatedUser.userId(), request));
    }

    @PostMapping("/history")
    public ApiResponse<EnglishLearningHistoryResponse> createLearningHistory(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody EnglishLearningHistoryCreateRequest request
    ) {
        return ApiResponse.success(englishEducationService.createLearningHistory(authenticatedUser.userId(), request));
    }
}
