package com.smart.classroom.smartclassroom.service;

import com.smart.classroom.smartclassroom.dto.AssignmentRequestDTO;
import com.smart.classroom.smartclassroom.dto.AssignmentResponseDTO;

public interface AssignmentService {

    AssignmentResponseDTO createAssignment(AssignmentRequestDTO assignmentRequestDTO);

    AssignmentResponseDTO viewAssignmentByTeacher(String email, Long classroomId, Long assignmentId);

    AssignmentResponseDTO viewAssignmentByStudent(String email, Long classroomId, Long assignmentId);

    AssignmentResponseDTO deleteAssignment(String email, Long classroomId, Long assignmentId);

}
