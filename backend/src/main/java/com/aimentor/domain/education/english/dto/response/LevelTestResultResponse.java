package com.aimentor.domain.education.english.dto.response;

import com.aimentor.domain.education.english.entity.EnglishLevel;
import com.aimentor.domain.education.english.entity.LevelTestResult;
import java.time.LocalDateTime;

public record LevelTestResultResponse(
        Long id,
        EnglishLevel level,
        Integer testScore,
        LocalDateTime testedAt
) {
    public static LevelTestResultResponse from(LevelTestResult result) {
        return new LevelTestResultResponse(
                result.getId(),
                result.getLevel(),
                result.getTestScore(),
                result.getTestedAt()
        );
    }
}
