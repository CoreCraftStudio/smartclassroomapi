package com.smart.classroom.smartclassroom.service;

import com.smart.classroom.smartclassroom.dto.ClassQuizMarkResponseDTO;
import com.smart.classroom.smartclassroom.dto.StudentQuizMarkResponseDTO;

public interface MarkService {

    ClassQuizMarkResponseDTO getClassQuizMarks(String studentUsername, Long classId);

    StudentQuizMarkResponseDTO getStudentQuizMarks(String teacherUsername, Long quizId);
}
