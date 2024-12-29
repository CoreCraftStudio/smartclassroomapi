package com.smart.classroom.smartclassroom.service.impl;

import com.smart.classroom.smartclassroom.dto.AssignmentRequestDTO;
import com.smart.classroom.smartclassroom.dto.AssignmentResponseDTO;
import com.smart.classroom.smartclassroom.entity.*;
import com.smart.classroom.smartclassroom.exception.AuthenticationException;
import com.smart.classroom.smartclassroom.exception.AuthorizationException;
import com.smart.classroom.smartclassroom.exception.ResourceNotFoundException;
import com.smart.classroom.smartclassroom.repository.AssignmentRepository;
import com.smart.classroom.smartclassroom.repository.ClassroomRepository;
import com.smart.classroom.smartclassroom.repository.UserRepository;
import com.smart.classroom.smartclassroom.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.smart.classroom.smartclassroom.util.Constant.UserConstant.TEACHER;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssignmentServiceImpl implements AssignmentService {

    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;
    private final ClassroomRepository classroomRepository;

    @Override
    public AssignmentResponseDTO createAssignment(String teacherEmail, AssignmentRequestDTO assignmentRequestDTO) {
        Long classroomId = assignmentRequestDTO.getClassroomId();
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classroomId);
        if (optionalClassroom.isPresent()) {
            Optional<Member> optionalTeacher = userRepository.findByEmail(teacherEmail);
            if (optionalTeacher.isPresent()) {
                Teacher teacher = (Teacher) optionalTeacher.get();
                if (teacher.getClassrooms().stream().map(Classroom::getId).collect(Collectors.toSet()).contains(classroomId)) {
                    Assignment assignment = assignmentRepository.save(Assignment.builder()
                            .name(assignmentRequestDTO.getName())
                            .description(assignmentRequestDTO.getDescription())
                            .attachmentId(assignmentRequestDTO.getAttachmentId())
                            .build());

                    Classroom classroom = optionalClassroom.get();
                    Set<Assignment> assignments = classroom.getAssignments();
                    assignments.add(assignment);
                    classroomRepository.save(classroom);

                    return AssignmentResponseDTO.builder()
                            .assignments(assignments)
                            .build();
                } else {
                    throw new AuthenticationException("Teacher not allow to add an assignment to the classroom");
                }

            } else {
                throw new ResourceNotFoundException("No teacher for given email");
            }
        } else {
            throw new ResourceNotFoundException("No classroom for given classroom id");
        }
    }

    @Override
    public AssignmentResponseDTO viewAssignments(String email, String type, Long classroomId) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classroomId);
        if (optionalClassroom.isPresent()) {
            Classroom classroom = optionalClassroom.get();
            Set<Assignment> assignments = classroom.getAssignments();
            if (TEACHER.equals(type)) {
                if (email.equals(classroom.getTeacher().getEmail())) {
                    return AssignmentResponseDTO.builder()
                            .assignments(assignments)
                            .build();
                } else {
                    throw new AuthorizationException("Teacher not allow to view the assignment");
                }
            } else {
                if (classroom.getStudents().stream().map(Student::getEmail).collect(Collectors.toSet()).contains(email)) {
                    assignments.stream()
                            .map(Assignment::getSubmissions)
                            .flatMap(Set::stream)
                            .collect(Collectors.toSet())
                            .removeIf(submission -> !email.equals(submission.getStudent().getEmail()));

                    return AssignmentResponseDTO.builder()
                            .assignments(assignments)
                            .build();
                } else {
                    throw new AuthorizationException("Student not allow to view the assignment");
                }
            }
        } else {
            throw new ResourceNotFoundException("No classroom for given id");
        }
    }

    @Override
    public AssignmentResponseDTO deleteAssignment(String email, Long classroomId, Long assignmentId) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classroomId);
        if (optionalClassroom.isPresent()) {
            Optional<Member> optionalTeacher = userRepository.findByEmail(email);
            if (optionalTeacher.isPresent()) {
                Teacher teacher = (Teacher) optionalTeacher.get();
                if (teacher.getClassrooms().stream().map(Classroom::getId).collect(Collectors.toSet()).contains(classroomId)) {
                    Optional<Assignment> assignmentOptional = assignmentRepository.findById(assignmentId);
                    if (assignmentOptional.isPresent()) {
                        assignmentRepository.deleteById(assignmentId);
                        Classroom classroom = optionalClassroom.get();
                        return AssignmentResponseDTO.builder()
                                .assignments(classroom.getAssignments())
                                .build();
                    } else {
                        throw new ResourceNotFoundException("No assignment for given id");
                    }
                } else {
                    throw new AuthorizationException("Teacher not allow to add an assignment to the classroom");
                }

            } else {
                throw new ResourceNotFoundException("No teacher for given email");
            }
        } else {
            throw new ResourceNotFoundException("No classroom for given id");
        }
    }
}
