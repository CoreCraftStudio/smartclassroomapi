package com.smart.classroom.smartclassroom.service;

import com.smart.classroom.smartclassroom.dto.AssignmentRequestDTO;
import com.smart.classroom.smartclassroom.dto.AssignmentResponseDTO;

public interface AssignmentService {

    AssignmentResponseDTO createAssignment(String teacherEmail, AssignmentRequestDTO assignmentRequestDTO);

    AssignmentResponseDTO viewAssignments(String email, String type, Long classroomId);

    AssignmentResponseDTO deleteAssignment(String email, Long classroomId, Long assignmentId);

}
