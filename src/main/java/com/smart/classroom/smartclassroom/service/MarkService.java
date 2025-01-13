package com.smart.classroom.smartclassroom.service;

import com.smart.classroom.smartclassroom.dto.QuizMarkResponseDTO;

public interface MarkService {

    QuizMarkResponseDTO getClassQuizMarks(String studentUsername, Long classId);

    QuizMarkResponseDTO getStudentQuizMarks(String teacherUsername, Long quizId);
}
