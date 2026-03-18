package com.aimentor.domain.education.history.entity;

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
@Table(name = "history_lessons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HistoryLesson extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private HistoryEra era;

    @Column(nullable = false, length = 10000)
    private String content;

    @Column(nullable = false)
    private Integer orderNum;

    @Builder
    public HistoryLesson(String title, HistoryEra era, String content, Integer orderNum) {
        this.title = title;
        this.era = era;
        this.content = content;
        this.orderNum = orderNum;
    }
}
