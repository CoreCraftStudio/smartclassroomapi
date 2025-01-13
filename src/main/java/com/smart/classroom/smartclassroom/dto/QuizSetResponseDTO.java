package com.smart.classroom.smartclassroom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizSetResponseDTO {
    Set<QuizDTO> quizzes;
}
