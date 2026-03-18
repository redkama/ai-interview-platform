package com.aimentor.domain.education.english.repository;

import com.aimentor.domain.education.english.entity.EnglishLesson;
import com.aimentor.domain.education.english.entity.EnglishLevel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnglishLessonRepository extends JpaRepository<EnglishLesson, Long> {

    List<EnglishLesson> findAllByLevelOrderByOrderNumAscIdAsc(EnglishLevel level);
}
