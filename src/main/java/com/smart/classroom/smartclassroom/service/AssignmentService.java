package com.smart.classroom.smartclassroom.service;

import com.smart.classroom.smartclassroom.dto.AssignmentRequestDTO;
import com.smart.classroom.smartclassroom.dto.AssignmentSetResponseDTO;

public interface AssignmentService {

    AssignmentSetResponseDTO createAssignment(String teacherUsername, AssignmentRequestDTO assignmentRequestDTO);

    AssignmentSetResponseDTO viewAssignments(String username, String type, Long classroomId);

    AssignmentSetResponseDTO deleteAssignment(String teacherUsername, Long classroomId, Long assignmentId);

}
