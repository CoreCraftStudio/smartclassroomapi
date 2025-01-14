package com.smart.classroom.smartclassroom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizRequestDTO {
    String name;
    String description;
    Long classroomId;
    @JsonProperty("questions")
    Set<QuestionDTO> questions;
}
