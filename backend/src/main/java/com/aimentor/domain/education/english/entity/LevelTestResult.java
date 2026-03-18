package com.aimentor.domain.education.english.entity;

import com.aimentor.common.entity.BaseTimeEntity;
import com.aimentor.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "english_level_test_results")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LevelTestResult extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnglishLevel level;

    @Column(nullable = false)
    private Integer testScore;

    @Column(nullable = false)
    private LocalDateTime testedAt;

    @Builder
    public LevelTestResult(User user, EnglishLevel level, Integer testScore, LocalDateTime testedAt) {
        this.user = user;
        this.level = level;
        this.testScore = testScore;
        this.testedAt = testedAt;
    }
}
