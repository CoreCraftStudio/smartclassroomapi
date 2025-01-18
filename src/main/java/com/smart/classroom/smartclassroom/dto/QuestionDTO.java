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
public class QuestionDTO {
    Long id;
    String question;
    Set<String> answers;
    Set<String> matchAnswers;
    Set<String> selectedAnswers;
    String type;
    Double mark;


}
