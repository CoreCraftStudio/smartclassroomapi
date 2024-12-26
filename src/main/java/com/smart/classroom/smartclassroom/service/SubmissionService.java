package com.smart.classroom.smartclassroom.service;

import com.smart.classroom.smartclassroom.dto.SubmissionRequestDTO;
import com.smart.classroom.smartclassroom.dto.SubmissionResponseDTO;

public interface SubmissionService {

    SubmissionResponseDTO createSubmission(SubmissionRequestDTO submissionRequestDTO);

    SubmissionResponseDTO deleteSubmission(String email, Long submissionId);
}
