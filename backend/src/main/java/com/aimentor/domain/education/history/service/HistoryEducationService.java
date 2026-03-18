package com.aimentor.domain.education.history.service;

import com.aimentor.common.exception.ApiException;
import com.aimentor.domain.education.history.dto.request.HistoryQuizAnswerRequest;
import com.aimentor.domain.education.history.dto.request.HistoryQuizSubmitRequest;
import com.aimentor.domain.education.history.dto.response.HistoryLearningHistoryResponse;
import com.aimentor.domain.education.history.dto.response.HistoryLessonResponse;
import com.aimentor.domain.education.history.dto.response.HistoryQuizResponse;
import com.aimentor.domain.education.history.dto.response.HistoryQuizSubmissionItemResponse;
import com.aimentor.domain.education.history.dto.response.HistoryQuizSubmitResponse;
import com.aimentor.domain.education.history.entity.HistoryEra;
import com.aimentor.domain.education.history.entity.HistoryLearningHistory;
import com.aimentor.domain.education.history.entity.HistoryLesson;
import com.aimentor.domain.education.history.entity.HistoryQuiz;
import com.aimentor.domain.education.history.repository.HistoryLearningHistoryRepository;
import com.aimentor.domain.education.history.repository.HistoryLessonRepository;
import com.aimentor.domain.education.history.repository.HistoryQuizRepository;
import com.aimentor.domain.user.entity.User;
import com.aimentor.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class HistoryEducationService {

    private final HistoryLessonRepository historyLessonRepository;
    private final HistoryQuizRepository historyQuizRepository;
    private final HistoryLearningHistoryRepository historyLearningHistoryRepository;
    private final UserRepository userRepository;

    public HistoryEducationService(
            HistoryLessonRepository historyLessonRepository,
            HistoryQuizRepository historyQuizRepository,
            HistoryLearningHistoryRepository historyLearningHistoryRepository,
            UserRepository userRepository
    ) {
        this.historyLessonRepository = historyLessonRepository;
        this.historyQuizRepository = historyQuizRepository;
        this.historyLearningHistoryRepository = historyLearningHistoryRepository;
        this.userRepository = userRepository;
    }

    public List<HistoryLessonResponse> getLessons(HistoryEra era) {
        return historyLessonRepository.findAllByEraOrderByOrderNumAscIdAsc(era)
                .stream()
                .map(HistoryLessonResponse::from)
                .toList();
    }

    public HistoryLessonResponse getLesson(Long lessonId) {
        return HistoryLessonResponse.from(getLessonEntity(lessonId));
    }

    public List<HistoryQuizResponse> getLessonQuiz(Long lessonId) {
        getLessonEntity(lessonId);
        return historyQuizRepository.findAllByLessonIdOrderByIdAsc(lessonId)
                .stream()
                .map(HistoryQuizResponse::from)
                .toList();
    }

    @Transactional
    public HistoryQuizSubmitResponse submitQuiz(Long userId, HistoryQuizSubmitRequest request) {
        User user = getUser(userId);
        HistoryLesson lesson = getLessonEntity(request.lessonId());
        List<HistoryQuiz> quizzes = historyQuizRepository.findAllByLessonIdOrderByIdAsc(lesson.getId());
        if (quizzes.isEmpty()) {
            throw new ApiException(HttpStatus.NOT_FOUND, "HISTORY_QUIZ_NOT_FOUND", "한국사 퀴즈를 찾을 수 없습니다.");
        }

        Map<Long, HistoryQuiz> quizById = quizzes.stream()
                .collect(Collectors.toMap(HistoryQuiz::getId, Function.identity()));

        List<HistoryQuizSubmissionItemResponse> results = request.answers().stream()
                .map(answer -> toSubmissionItem(quizById, answer))
                .toList();

        int correctCount = (int) results.stream()
                .filter(HistoryQuizSubmissionItemResponse::correct)
                .count();
        int score = (int) Math.round((correctCount * 100.0) / quizzes.size());
        LocalDateTime completedAt = LocalDateTime.now();

        HistoryLearningHistory history = historyLearningHistoryRepository.save(HistoryLearningHistory.builder()
                .user(user)
                .lesson(lesson)
                .quizScore(score)
                .completedAt(completedAt)
                .build());

        return new HistoryQuizSubmitResponse(
                history.getId(),
                lesson.getId(),
                score,
                completedAt,
                results
        );
    }

    public List<HistoryLearningHistoryResponse> getMyHistories(Long userId) {
        return historyLearningHistoryRepository.findAllByUserIdOrderByCompletedAtDescIdDesc(userId)
                .stream()
                .map(HistoryLearningHistoryResponse::from)
                .toList();
    }

    private HistoryQuizSubmissionItemResponse toSubmissionItem(
            Map<Long, HistoryQuiz> quizById,
            HistoryQuizAnswerRequest answer
    ) {
        HistoryQuiz quiz = quizById.get(answer.quizId());
        if (quiz == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "INVALID_HISTORY_QUIZ", "해당 레슨에 속하지 않는 퀴즈가 포함되어 있습니다.");
        }
        boolean correct = quiz.getAnswer().equals(answer.selectedAnswer());
        return new HistoryQuizSubmissionItemResponse(
                quiz.getId(),
                answer.selectedAnswer(),
                quiz.getAnswer(),
                correct,
                quiz.getExplanation()
        );
    }

    private HistoryLesson getLessonEntity(Long lessonId) {
        return historyLessonRepository.findById(lessonId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "HISTORY_LESSON_NOT_FOUND", "한국사 학습 레슨을 찾을 수 없습니다."));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "사용자를 찾을 수 없습니다."));
    }
}
