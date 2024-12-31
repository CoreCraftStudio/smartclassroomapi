package com.smart.classroom.smartclassroom.service;

import com.smart.classroom.smartclassroom.dto.AssignmentRequestDTO;
import com.smart.classroom.smartclassroom.dto.AssignmentResponseDTO;

public interface AssignmentService {

    AssignmentResponseDTO createAssignment(String teacherUsername, AssignmentRequestDTO assignmentRequestDTO);

    AssignmentResponseDTO viewAssignments(String username, String type, Long classroomId);

    AssignmentResponseDTO deleteAssignment(String teacherUsername, Long classroomId, Long assignmentId);

}
