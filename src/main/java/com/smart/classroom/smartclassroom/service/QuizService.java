package com.smart.classroom.smartclassroom.service;

import com.smart.classroom.smartclassroom.dto.QuizRequestDTO;
import com.smart.classroom.smartclassroom.dto.QuizResponseDTO;

public interface QuizService {

    QuizResponseDTO createQuiz(QuizRequestDTO quizRequestDTO);

    QuizResponseDTO deleteQuiz(String email, Long classroomId, Long quizId);


}
