package com.smart.classroom.smartclassroom.service;

import com.smart.classroom.smartclassroom.dto.QuizRequestDTO;
import com.smart.classroom.smartclassroom.dto.QuizResponseDTO;

public interface QuizService {

    QuizResponseDTO createQuiz(String teacherEmail, QuizRequestDTO quizRequestDTO);

    QuizResponseDTO deleteQuiz(String teacherEmail, Long classroomId, Long quizId);


}
