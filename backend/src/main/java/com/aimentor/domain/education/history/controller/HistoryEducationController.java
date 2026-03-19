package com.aimentor.domain.education.history.controller;

import com.aimentor.common.api.ApiResponse;
import com.aimentor.common.security.AuthenticatedUser;
import com.aimentor.domain.education.history.dto.request.HistoryExplainRequest;
import com.aimentor.domain.education.history.dto.request.HistoryQuizSubmitRequest;
import com.aimentor.domain.education.history.dto.response.HistoryExplainResponse;
import com.aimentor.domain.education.history.dto.response.HistoryLearningHistoryResponse;
import com.aimentor.domain.education.history.dto.response.HistoryLessonResponse;
import com.aimentor.domain.education.history.dto.response.HistoryQuizResponse;
import com.aimentor.domain.education.history.dto.response.HistoryQuizSubmitResponse;
import com.aimentor.domain.education.history.entity.HistoryEra;
import com.aimentor.domain.education.history.service.HistoryEducationService;
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
@RequestMapping("/api/education/history")
public class HistoryEducationController {

    private final HistoryEducationService historyEducationService;

    public HistoryEducationController(HistoryEducationService historyEducationService) {
        this.historyEducationService = historyEducationService;
    }

    @GetMapping("/lessons")
    public ApiResponse<List<HistoryLessonResponse>> getLessons(@RequestParam HistoryEra era) {
        return ApiResponse.success(historyEducationService.getLessons(era));
    }

    @GetMapping("/lessons/{id}")
    public ApiResponse<HistoryLessonResponse> getLesson(@PathVariable Long id) {
        return ApiResponse.success(historyEducationService.getLesson(id));
    }

    @GetMapping("/lessons/{id}/quiz")
    public ApiResponse<List<HistoryQuizResponse>> getLessonQuiz(@PathVariable Long id) {
        return ApiResponse.success(historyEducationService.getLessonQuiz(id));
    }

    @PostMapping("/explain")
    public ApiResponse<HistoryExplainResponse> generateExplanation(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody HistoryExplainRequest request
    ) {
        return ApiResponse.success(historyEducationService.generateExplanation(request));
    }

    @PostMapping("/quiz/submit")
    public ApiResponse<HistoryQuizSubmitResponse> submitQuiz(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody HistoryQuizSubmitRequest request
    ) {
        return ApiResponse.success(historyEducationService.submitQuiz(authenticatedUser.userId(), request));
    }

    @GetMapping("/my-history")
    public ApiResponse<List<HistoryLearningHistoryResponse>> getMyHistory(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        return ApiResponse.success(historyEducationService.getMyHistories(authenticatedUser.userId()));
    }
}
