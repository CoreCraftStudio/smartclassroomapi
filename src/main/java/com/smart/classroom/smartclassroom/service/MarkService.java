package com.smart.classroom.smartclassroom.service;

import com.smart.classroom.smartclassroom.dto.QuizReportResponseDTO;

public interface MarkService {

    QuizReportResponseDTO getQuizMarks(String teacherUsername, Long quizId);
}
