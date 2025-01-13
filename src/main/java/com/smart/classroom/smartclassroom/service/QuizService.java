package com.smart.classroom.smartclassroom.service;

import com.smart.classroom.smartclassroom.dto.QuizRequestDTO;
import com.smart.classroom.smartclassroom.dto.QuizResponseDTO;
import com.smart.classroom.smartclassroom.dto.QuizSetResponseDTO;

public interface QuizService {

    QuizSetResponseDTO createQuiz(String teacherUsername, QuizRequestDTO quizRequestDTO);

    QuizSetResponseDTO deleteQuiz(String teacherUsername, Long quizId);

    QuizSetResponseDTO viewQuizzes(String username, String type, Long classroomId);

    QuizResponseDTO viewQuiz(String username, String type, Long quizId);


}
