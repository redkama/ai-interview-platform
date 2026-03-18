package com.aimentor.domain.education.english.entity;

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
@Table(name = "english_learning_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EnglishLearningHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lesson_id")
    private EnglishLesson lesson;

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false)
    private LocalDateTime completedAt;

    @Builder
    public EnglishLearningHistory(User user, EnglishLesson lesson, Integer score, LocalDateTime completedAt) {
        this.user = user;
        this.lesson = lesson;
        this.score = score;
        this.completedAt = completedAt;
    }
}
