package com.smart.classroom.smartclassroom.repository;

import com.smart.classroom.smartclassroom.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
