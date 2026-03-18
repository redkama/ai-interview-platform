package com.aimentor.domain.education.history.dto.response;

import com.aimentor.domain.education.history.entity.HistoryEra;
import com.aimentor.domain.education.history.entity.HistoryLesson;

public record HistoryLessonResponse(
        Long id,
        String title,
        HistoryEra era,
        String content,
        Integer orderNum
) {
    public static HistoryLessonResponse from(HistoryLesson lesson) {
        return new HistoryLessonResponse(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getEra(),
                lesson.getContent(),
                lesson.getOrderNum()
        );
    }
}
