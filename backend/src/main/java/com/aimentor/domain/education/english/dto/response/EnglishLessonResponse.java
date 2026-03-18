package com.aimentor.domain.education.english.dto.response;

import com.aimentor.domain.education.english.entity.EnglishLesson;
import com.aimentor.domain.education.english.entity.EnglishLevel;

public record EnglishLessonResponse(
        Long id,
        String title,
        String part,
        EnglishLevel level,
        String content,
        Integer orderNum
) {
    public static EnglishLessonResponse from(EnglishLesson lesson) {
        return new EnglishLessonResponse(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getPart(),
                lesson.getLevel(),
                lesson.getContent(),
                lesson.getOrderNum()
        );
    }
}
