package com.smart.classroom.smartclassroom.service.impl;

import com.smart.classroom.smartclassroom.dto.AssignmentDTO;
import com.smart.classroom.smartclassroom.dto.AssignmentRequestDTO;
import com.smart.classroom.smartclassroom.dto.AssignmentSetResponseDTO;
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
    public AssignmentSetResponseDTO createAssignment(String teacherUsername, AssignmentRequestDTO assignmentRequestDTO) {
        Long classroomId = assignmentRequestDTO.getClassroomId();
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classroomId);
        if (optionalClassroom.isPresent()) {
            Optional<Member> optionalTeacher = userRepository.findByUsername(teacherUsername);
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

                    return AssignmentSetResponseDTO.builder()
                            .assignments(assignments.stream()
                                    .map(a -> AssignmentDTO.builder()
                                            .id(a.getId())
                                            .name(a.getName())
                                            .description(a.getDescription())
                                            .build())
                                    .collect(Collectors.toSet()))
                            .build();
                } else {
                    throw new AuthenticationException("Teacher not allow to add an assignment to the classroom");
                }

            } else {
                throw new ResourceNotFoundException("No teacher for given username");
            }
        } else {
            throw new ResourceNotFoundException("No classroom for given classroom id");
        }
    }

    @Override
    public AssignmentSetResponseDTO viewAssignments(String username, String type, Long classroomId) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classroomId);
        if (optionalClassroom.isPresent()) {
            Classroom classroom = optionalClassroom.get();
            Set<Assignment> assignments = classroom.getAssignments();
            if (TEACHER.equals(type)) {
                if (username.equals(classroom.getTeacher().getUsername())) {
                    return AssignmentSetResponseDTO.builder()
                            .assignments(assignments.stream()
                                    .map(assignment -> AssignmentDTO.builder()
                                            .id(assignment.getId())
                                            .name(assignment.getName())
                                            .description(assignment.getDescription())
                                            .build())
                                    .collect(Collectors.toSet()))
                            .build();
                } else {
                    throw new AuthorizationException("Teacher not allow to view the assignment");
                }
            } else {
                if (classroom.getStudents().stream().map(Student::getUsername).collect(Collectors.toSet()).contains(username)) {
                    assignments.stream()
                            .map(Assignment::getSubmissions)
                            .flatMap(Set::stream)
                            .collect(Collectors.toSet())
                            .removeIf(submission -> !username.equals(submission.getStudent().getUsername()));

                    return AssignmentSetResponseDTO.builder()
                            .assignments(assignments.stream()
                                    .map(assignment -> AssignmentDTO.builder()
                                            .id(assignment.getId())
                                            .name(assignment.getName())
                                            .description(assignment.getDescription())
                                            .build())
                                    .collect(Collectors.toSet()))
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
    public AssignmentSetResponseDTO deleteAssignment(String teacherUsername, Long classroomId, Long assignmentId) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classroomId);
        if (optionalClassroom.isPresent()) {
            Optional<Member> optionalTeacher = userRepository.findByUsername(teacherUsername);
            if (optionalTeacher.isPresent()) {
                Teacher teacher = (Teacher) optionalTeacher.get();
                if (teacher.getClassrooms().stream().map(Classroom::getId).collect(Collectors.toSet()).contains(classroomId)) {
                    Optional<Assignment> assignmentOptional = assignmentRepository.findById(assignmentId);
                    if (assignmentOptional.isPresent()) {
                        assignmentRepository.deleteById(assignmentId);
                        Classroom classroom = optionalClassroom.get();
                        return AssignmentSetResponseDTO.builder()
                                .assignments(classroom.getAssignments().stream()
                                        .map(assignment -> AssignmentDTO.builder()
                                                .id(assignment.getId())
                                                .name(assignment.getName())
                                                .description(assignment.getDescription())
                                                .build())
                                        .collect(Collectors.toSet()))
                                .build();
                    } else {
                        throw new ResourceNotFoundException("No assignment for given id");
                    }
                } else {
                    throw new AuthorizationException("Teacher not allow to add an assignment to the classroom");
                }

            } else {
                throw new ResourceNotFoundException("No teacher for given username");
            }
        } else {
            throw new ResourceNotFoundException("No classroom for given id");
        }
    }
}
