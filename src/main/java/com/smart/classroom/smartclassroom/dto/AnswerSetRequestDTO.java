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
public class AnswerSetRequestDTO {

    Long classroomId;
    String email;
    Set<AnswerRequestDTO> answerSet;
}
