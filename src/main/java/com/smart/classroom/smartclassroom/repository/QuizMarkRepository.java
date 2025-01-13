package com.smart.classroom.smartclassroom.repository;

import com.smart.classroom.smartclassroom.entity.QuizMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface QuizMarkRepository extends JpaRepository<QuizMark, Long> {

    @Query("select qm from QuizMark qm where qm.student.username=?1 and qm.quiz.classroom.id=?2")
    Set<QuizMark> findByUsernameAndClassId(String username, Long classroomId);

    @Query("select qm from QuizMark qm where qm.quiz.id=?1")
    Set<QuizMark> findByQuizId(Long quizId);
}
