package com.smart.classroom.smartclassroom.service;

import com.smart.classroom.smartclassroom.dto.QuizMarkResponseDTO;

public interface MarkService {

    QuizMarkResponseDTO getQuizMarks(String teacherUsername, Long quizId);
}
