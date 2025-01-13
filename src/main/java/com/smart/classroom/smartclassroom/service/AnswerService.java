package com.smart.classroom.smartclassroom.service;

import com.smart.classroom.smartclassroom.dto.AnswerSetRequestDTO;
import com.smart.classroom.smartclassroom.dto.QuizSetResponseDTO;

public interface AnswerService {

    QuizSetResponseDTO createAnswerSet(String studentUsername, AnswerSetRequestDTO answerSetRequestDTO);
}
