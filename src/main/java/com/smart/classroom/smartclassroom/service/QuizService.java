package com.smart.classroom.smartclassroom.service;

import com.smart.classroom.smartclassroom.dto.QuizRequestDTO;
import com.smart.classroom.smartclassroom.dto.QuizResponseDTO;

public interface QuizService {

    QuizResponseDTO createQuiz(String teacherUsername, QuizRequestDTO quizRequestDTO);

    QuizResponseDTO deleteQuiz(String teacherUsername, Long quizId);

    QuizResponseDTO viewQuizzes(String username, String type, Long classroomId);


}
