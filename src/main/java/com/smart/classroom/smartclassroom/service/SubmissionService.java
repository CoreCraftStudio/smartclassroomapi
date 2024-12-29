package com.smart.classroom.smartclassroom.service;

import com.smart.classroom.smartclassroom.dto.AssignmentResponseDTO;
import com.smart.classroom.smartclassroom.dto.SubmissionRequestDTO;

public interface SubmissionService {

    AssignmentResponseDTO createSubmission(String studentEmail, SubmissionRequestDTO submissionRequestDTO);

    AssignmentResponseDTO deleteSubmission(String studentEmail, String classroomId, Long submissionId);
}
