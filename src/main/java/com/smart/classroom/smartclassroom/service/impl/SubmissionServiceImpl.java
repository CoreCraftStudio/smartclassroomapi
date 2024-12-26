package com.smart.classroom.smartclassroom.service.impl;

import com.smart.classroom.smartclassroom.dto.SubmissionRequestDTO;
import com.smart.classroom.smartclassroom.dto.SubmissionResponseDTO;
import com.smart.classroom.smartclassroom.entity.Assignment;
import com.smart.classroom.smartclassroom.entity.Student;
import com.smart.classroom.smartclassroom.entity.Submission;
import com.smart.classroom.smartclassroom.entity.User;
import com.smart.classroom.smartclassroom.exception.ValidationException;
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
    public SubmissionResponseDTO createSubmission(SubmissionRequestDTO submissionRequestDTO) {
        Optional<Assignment> optionalAssignment = assignmentRepository.findById(submissionRequestDTO.getAssignmentId());
        if (optionalAssignment.isPresent()) {
            Optional<User> userOptional = userRepository.findByEmail(submissionRequestDTO.getEmail());
            if (userOptional.isPresent()) {
                Student student = (Student) userOptional.get();
                Submission submission = submissionRepository.save(Submission.builder()
                        .attachmentId(submissionRequestDTO.getAttachmentId())
                        .student(student)
                        .build());

                Assignment assignment = optionalAssignment.get();
                assignment.getSubmissions().add(submission);
                assignmentRepository.save(assignment);
                return SubmissionResponseDTO.builder()
                        .id(submission.getId())
                        .build();

            } else {
                throw new ValidationException("No Student for given email");
            }
        } else {
            throw new ValidationException("No assignment for given id");
        }
    }

    @Override
    public SubmissionResponseDTO deleteSubmission(String email, Long submissionId) {
        Optional<Submission> optionalSubmission = submissionRepository.findById(submissionId);
        if (optionalSubmission.isPresent()) {
            Submission submission = optionalSubmission.get();
            if (submission.getStudent().getEmail().equals(email)) {
                assignmentRepository.deleteById(submissionId);
                return SubmissionResponseDTO.builder()
                        .id(submission.getId())
                        .build();
            } else {
                throw new ValidationException("Student not allow to delete the submission");
            }

        } else {
            throw new ValidationException("No Submission for given id");
        }
    }

}
