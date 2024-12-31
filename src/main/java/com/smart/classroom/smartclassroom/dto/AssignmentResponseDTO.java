package com.smart.classroom.smartclassroom.dto;

import com.smart.classroom.smartclassroom.entity.Assignment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentResponseDTO {
    Set<Assignment> assignments;
}
