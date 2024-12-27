package com.smart.classroom.smartclassroom.service;

import com.smart.classroom.smartclassroom.dto.AnswerSetRequestDTO;
import com.smart.classroom.smartclassroom.dto.QuizResponseDTO;

public interface AnswerService {

    QuizResponseDTO createAnswerSet(String studentEmail, AnswerSetRequestDTO answerSetRequestDTO);
}
