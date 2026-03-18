package com.aimentor.domain.education.history.entity;

import com.aimentor.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "history_quizzes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HistoryQuiz extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lesson_id")
    private HistoryLesson lesson;

    @Column(nullable = false, length = 2000)
    private String question;

    @Column(nullable = false, length = 4000)
    private String options;

    @Column(nullable = false, length = 500)
    private String answer;

    @Column(nullable = false, length = 4000)
    private String explanation;

    @Builder
    public HistoryQuiz(HistoryLesson lesson, String question, String options, String answer, String explanation) {
        this.lesson = lesson;
        this.question = question;
        this.options = options;
        this.answer = answer;
        this.explanation = explanation;
    }
}
