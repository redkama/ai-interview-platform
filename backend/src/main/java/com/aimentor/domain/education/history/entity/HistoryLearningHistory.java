package com.aimentor.domain.education.history.entity;

import com.aimentor.common.entity.BaseTimeEntity;
import com.aimentor.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "history_learning_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HistoryLearningHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lesson_id")
    private HistoryLesson lesson;

    @Column(nullable = false)
    private Integer quizScore;

    @Column(nullable = false)
    private LocalDateTime completedAt;

    @Builder
    public HistoryLearningHistory(User user, HistoryLesson lesson, Integer quizScore, LocalDateTime completedAt) {
        this.user = user;
        this.lesson = lesson;
        this.quizScore = quizScore;
        this.completedAt = completedAt;
    }
}
