package com.smart.classroom.smartclassroom.service.impl;

import com.smart.classroom.smartclassroom.dto.AssignmentRequestDTO;
import com.smart.classroom.smartclassroom.dto.AssignmentResponseDTO;
import com.smart.classroom.smartclassroom.entity.*;
import com.smart.classroom.smartclassroom.exception.ValidationException;
import com.smart.classroom.smartclassroom.repository.AssignmentRepository;
import com.smart.classroom.smartclassroom.repository.ClassroomRepository;
import com.smart.classroom.smartclassroom.repository.UserRepository;
import com.smart.classroom.smartclassroom.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssignmentServiceImpl implements AssignmentService {

    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;
    private final ClassroomRepository classroomRepository;

    @Override
    public AssignmentResponseDTO createAssignment(AssignmentRequestDTO assignmentRequestDTO) {
        Long classroomId = assignmentRequestDTO.getClassroomId();
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classroomId);
        if (optionalClassroom.isPresent()) {
            Optional<Member> optionalTeacher = userRepository.findByEmail(assignmentRequestDTO.getEmail());
            if (optionalTeacher.isPresent()) {
                Teacher teacher = (Teacher) optionalTeacher.get();
                if (teacher.getClassrooms().stream().map(Classroom::getId).toList().contains(classroomId)) {
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
                            .id(assignment.getId())
                            .name(assignment.getName())
                            .description(assignment.getDescription())
                            .attachmentId(assignment.getAttachmentId())
                            .build();
                } else {
                    throw new ValidationException("Teacher not allow to add an assignment to the classroom");
                }

            } else {
                throw new ValidationException("No teacher for given email");
            }
        } else {
            throw new ValidationException("No classroom for given classroom id");
        }
    }

    @Override
    public AssignmentResponseDTO viewAssignmentByTeacher(String email, Long classroomId, Long assignmentId) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classroomId);
        if (optionalClassroom.isPresent()) {
            Optional<Assignment> assignmentOptional = assignmentRepository.findById(assignmentId);
            if (assignmentOptional.isPresent()) {
                Optional<Member> optionalUser = userRepository.findByEmail(email);
                if (optionalUser.isPresent()) {
                    Teacher teacher = (Teacher) optionalUser.get();
                    if (teacher.getClassrooms().stream().map(Classroom::getId).toList().contains(classroomId)) {
                        assignmentRepository.findById(assignmentId);
                        Assignment assignment = assignmentOptional.get();
                        return AssignmentResponseDTO.builder()
                                .id(assignmentId)
                                .name(assignment.getName())
                                .description(assignment.getDescription())
                                .attachmentId(assignment.getAttachmentId())
                                .build();
                    } else {
                        throw new ValidationException("Teacher not allow to view the assignment");
                    }

                } else {
                    throw new ValidationException("No user for given email");
                }
            } else {
                throw new ValidationException("No assignment for given id");
            }
        } else {
            throw new ValidationException("No classroom for given classroom id");
        }
    }

    @Override
    public AssignmentResponseDTO viewAssignmentByStudent(String email, Long classroomId, Long assignmentId) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classroomId);
        if (optionalClassroom.isPresent()) {
            Optional<Assignment> assignmentOptional = assignmentRepository.findById(assignmentId);
            if (assignmentOptional.isPresent()) {
                Optional<Member> optionalUser = userRepository.findByEmail(email);
                if (optionalUser.isPresent()) {
                    Classroom classroom = optionalClassroom.get();
                    if (classroom.getStudents().stream().map(Student::getEmail).toList().contains(email)) {
                        assignmentRepository.deleteById(assignmentId);
                        return AssignmentResponseDTO.builder()
                                .id(assignmentId)
                                .build();
                    } else {
                        throw new ValidationException("Student not allow to view the assignment");
                    }
                } else {
                    throw new ValidationException("No user for given email");
                }
            } else {
                throw new ValidationException("No assignment for given id");
            }
        } else {
            throw new ValidationException("No classroom for given classroom id");
        }
    }


    @Override
    public AssignmentResponseDTO deleteAssignment(String email, Long classroomId, Long assignmentId) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classroomId);
        if (optionalClassroom.isPresent()) {
            Optional<Member> optionalTeacher = userRepository.findByEmail(email);
            if (optionalTeacher.isPresent()) {
                Teacher teacher = (Teacher) optionalTeacher.get();
                if (teacher.getClassrooms().stream().map(Classroom::getId).toList().contains(classroomId)) {
                    Optional<Assignment> assignmentOptional = assignmentRepository.findById(assignmentId);
                    if (assignmentOptional.isPresent()) {
                        assignmentRepository.deleteById(assignmentId);
                        return AssignmentResponseDTO.builder()
                                .id(assignmentId)
                                .build();
                    } else {
                        throw new ValidationException("No assignment for given id");
                    }
                } else {
                    throw new ValidationException("Teacher not allow to add an assignment to the classroom");
                }

            } else {
                throw new ValidationException("No teacher for given email");
            }
        } else {
            throw new ValidationException("No classroom for given classroom id");
        }
    }
}
