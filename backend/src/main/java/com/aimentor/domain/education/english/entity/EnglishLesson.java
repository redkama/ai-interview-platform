package com.aimentor.domain.education.english.entity;

import com.aimentor.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "english_lessons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EnglishLesson extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 100)
    private String part;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnglishLevel level;

    @Column(nullable = false, length = 10000)
    private String content;

    @Column(nullable = false)
    private Integer orderNum;

    @Builder
    public EnglishLesson(String title, String part, EnglishLevel level, String content, Integer orderNum) {
        this.title = title;
        this.part = part;
        this.level = level;
        this.content = content;
        this.orderNum = orderNum;
    }
}
