package com.aimentor.domain.education.english.service;

import com.aimentor.common.exception.ApiException;
import com.aimentor.domain.education.english.dto.request.EnglishLearningHistoryCreateRequest;
import com.aimentor.domain.education.english.dto.request.EnglishLevelTestRequest;
import com.aimentor.domain.education.english.dto.response.EnglishLearningHistoryResponse;
import com.aimentor.domain.education.english.dto.response.EnglishLessonResponse;
import com.aimentor.domain.education.english.dto.response.LevelTestResultResponse;
import com.aimentor.domain.education.english.entity.EnglishLearningHistory;
import com.aimentor.domain.education.english.entity.EnglishLesson;
import com.aimentor.domain.education.english.entity.EnglishLevel;
import com.aimentor.domain.education.english.entity.LevelTestResult;
import com.aimentor.domain.education.english.repository.EnglishLearningHistoryRepository;
import com.aimentor.domain.education.english.repository.EnglishLessonRepository;
import com.aimentor.domain.education.english.repository.LevelTestResultRepository;
import com.aimentor.domain.user.entity.User;
import com.aimentor.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EnglishEducationService {

    private final EnglishLessonRepository englishLessonRepository;
    private final EnglishLearningHistoryRepository englishLearningHistoryRepository;
    private final LevelTestResultRepository levelTestResultRepository;
    private final UserRepository userRepository;

    public EnglishEducationService(
            EnglishLessonRepository englishLessonRepository,
            EnglishLearningHistoryRepository englishLearningHistoryRepository,
            LevelTestResultRepository levelTestResultRepository,
            UserRepository userRepository
    ) {
        this.englishLessonRepository = englishLessonRepository;
        this.englishLearningHistoryRepository = englishLearningHistoryRepository;
        this.levelTestResultRepository = levelTestResultRepository;
        this.userRepository = userRepository;
    }

    public List<EnglishLessonResponse> getLessons(EnglishLevel level) {
        return englishLessonRepository.findAllByLevelOrderByOrderNumAscIdAsc(level)
                .stream()
                .map(EnglishLessonResponse::from)
                .toList();
    }

    public EnglishLessonResponse getLesson(Long lessonId) {
        return EnglishLessonResponse.from(getLessonEntity(lessonId));
    }

    @Transactional
    public LevelTestResultResponse createLevelTestResult(Long userId, EnglishLevelTestRequest request) {
        User user = getUser(userId);
        LevelTestResult result = levelTestResultRepository.save(LevelTestResult.builder()
                .user(user)
                .level(resolveLevel(request.testScore()))
                .testScore(request.testScore())
                .testedAt(LocalDateTime.now())
                .build());
        return LevelTestResultResponse.from(result);
    }

    @Transactional
    public EnglishLearningHistoryResponse createLearningHistory(Long userId, EnglishLearningHistoryCreateRequest request) {
        User user = getUser(userId);
        EnglishLesson lesson = getLessonEntity(request.lessonId());
        EnglishLearningHistory history = englishLearningHistoryRepository.save(EnglishLearningHistory.builder()
                .user(user)
                .lesson(lesson)
                .score(request.score())
                .completedAt(LocalDateTime.now())
                .build());
        return EnglishLearningHistoryResponse.from(history);
    }

    private EnglishLesson getLessonEntity(Long lessonId) {
        return englishLessonRepository.findById(lessonId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "ENGLISH_LESSON_NOT_FOUND", "영어 학습 레슨을 찾을 수 없습니다."));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "사용자를 찾을 수 없습니다."));
    }

    private EnglishLevel resolveLevel(int testScore) {
        if (testScore >= 80) {
            return EnglishLevel.ADVANCED;
        }
        if (testScore >= 50) {
            return EnglishLevel.INTERMEDIATE;
        }
        return EnglishLevel.BEGINNER;
    }
}
