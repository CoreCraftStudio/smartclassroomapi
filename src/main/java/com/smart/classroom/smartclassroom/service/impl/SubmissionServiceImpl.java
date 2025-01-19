package com.smart.classroom.smartclassroom.service.impl;

import com.smart.classroom.smartclassroom.dto.AssignmentDTO;
import com.smart.classroom.smartclassroom.dto.AssignmentResponseDTO;
import com.smart.classroom.smartclassroom.dto.SubmissionRequestDTO;
import com.smart.classroom.smartclassroom.entity.Assignment;
import com.smart.classroom.smartclassroom.entity.Member;
import com.smart.classroom.smartclassroom.entity.Student;
import com.smart.classroom.smartclassroom.entity.Submission;
import com.smart.classroom.smartclassroom.exception.ResourceNotFoundException;
import com.smart.classroom.smartclassroom.repository.AssignmentRepository;
import com.smart.classroom.smartclassroom.repository.SubmissionRepository;
import com.smart.classroom.smartclassroom.repository.UserRepository;
import com.smart.classroom.smartclassroom.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {

    private final UserRepository userRepository;
    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;

    @Override
    public AssignmentResponseDTO createSubmission(String studentUsername, SubmissionRequestDTO submissionRequestDTO) {
        Optional<Member> optionalStudent = userRepository.findByUsername(studentUsername);
        Optional<Assignment> optionalAssignment = assignmentRepository.findById(submissionRequestDTO.getAssignmentId());
        if (optionalStudent.isPresent() && optionalAssignment.isPresent()) {
            Student student = (Student) optionalStudent.get();
            Assignment assignment = optionalAssignment.get();
            Submission submission = submissionRepository.save(Submission.builder()
                    .assignment(assignment)
                    .description(submissionRequestDTO.getDescription())
                    .attachmentId(submissionRequestDTO.getAttachmentId())
                    .student(student)
                    .build());

            submissionRepository.save(submission);
            return AssignmentResponseDTO.builder()
                    .assignment(AssignmentDTO.builder()
                            .id(assignment.getId())
                            .name(assignment.getName())
                            .name(assignment.getDescription())
                            .attachmentId(assignment.getAttachmentId())
                            .maxMark(assignment.getMaxMark())
                            .build())
                    .build();
        } else {
            throw new ResourceNotFoundException("No assignment for given id");
        }
    }

    @Override
    public AssignmentResponseDTO deleteSubmission(String studentUsername, String classroomId, Long submissionId) {
        Optional<Submission> optionalSubmission = submissionRepository.findById(submissionId);
        if (optionalSubmission.isPresent()) {
            Submission submission = optionalSubmission.get();
            if (submission.getStudent().getUsername().equals(studentUsername)) {
                Assignment assignment = submission.getAssignment();
                assignmentRepository.deleteById(submissionId);
                return AssignmentResponseDTO.builder()
                        .assignment(AssignmentDTO.builder()
                                .id(assignment.getId())
                                .name(assignment.getName())
                                .description(assignment.getDescription())
                                .attachmentId(assignment.getAttachmentId())
                                .maxMark(assignment.getMaxMark())
                                .build())
                        .build();
            } else {
                throw new ResourceNotFoundException("Student not allow to delete the submission");
            }

        } else {
            throw new ResourceNotFoundException("No Submission for given id");
        }
    }

}
