package com.smart.classroom.smartclassroom.service.impl;

import com.smart.classroom.smartclassroom.dto.*;
import com.smart.classroom.smartclassroom.entity.*;
import com.smart.classroom.smartclassroom.exception.AuthorizationException;
import com.smart.classroom.smartclassroom.exception.ResourceNotFoundException;
import com.smart.classroom.smartclassroom.repository.ClassroomRepository;
import com.smart.classroom.smartclassroom.repository.UserRepository;
import com.smart.classroom.smartclassroom.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.smart.classroom.smartclassroom.util.Constant.ExceptionMessage.NO_CLASSROOM_FOR_ID;
import static com.smart.classroom.smartclassroom.util.Constant.ExceptionMessage.NO_STUDENT_FOR_USERNAME;
import static com.smart.classroom.smartclassroom.util.Constant.UserConstant.TEACHER;

@Service
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;

    @Override
    public ClassroomResponseDTO createClassroom(String teacherUsername, ClassroomRequestDTO classroomRequestDTO) {
        Optional<Member> optionalTeacher = userRepository.findByUsername(teacherUsername);
        if (optionalTeacher.isPresent()) {
            Teacher teacher = (Teacher) optionalTeacher.get();
            classroomRepository.save(Classroom.builder()
                    .teacher(teacher)
                    .name(classroomRequestDTO.getName())
                    .build());

            return ClassroomResponseDTO.builder()
                    .classrooms(teacher.getClassrooms().stream()
                            .map(classroom -> ClassroomDTO.builder()
                                    .id(classroom.getId())
                                    .name(classroom.getName())
                                    .build())
                            .collect(Collectors.toSet()))
                    .build();
        } else {
            throw new ResourceNotFoundException("No teacher for given username");
        }
    }

    @Override
    public ClassroomResponseDTO deleteClassroom(String teacherUsername, Long classroomId) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classroomId);
        if (optionalClassroom.isPresent()) {
            Classroom classroom = optionalClassroom.get();
            Teacher teacher = classroom.getTeacher();
            if (teacherUsername.equals(teacher.getUsername())) {
                classroomRepository.deleteById(classroomId);
                return ClassroomResponseDTO.builder()
                        .classrooms(teacher.getClassrooms().stream()
                                .map(cl -> ClassroomDTO.builder()
                                        .id(cl.getId())
                                        .name(cl.getName())
                                        .build())
                                .collect(Collectors.toSet()))
                        .build();
            } else {
                throw new AuthorizationException("Teacher not allow to delete the classroom");
            }
        } else {
            throw new ResourceNotFoundException(NO_CLASSROOM_FOR_ID);
        }
    }

    @Override
    public StudentSetResponseDTO addStudent(String teacherUsername, String studentUsername, Long classroomId) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classroomId);
        if (optionalClassroom.isPresent()) {
            Classroom classroom = optionalClassroom.get();
            Teacher teacher = classroom.getTeacher();
            if (teacherUsername.equals(teacher.getUsername())) {
                Optional<Member> optionalStudent = userRepository.findByUsername(studentUsername);
                if (optionalStudent.isPresent()) {
                    Student student = (Student) optionalStudent.get();
                    Set<Student> students = classroom.getStudents();
                    students.add(student);
                    classroom.setStudents(students);
                    classroomRepository.save(classroom);
                    return StudentSetResponseDTO.builder()
                            .students(classroom.getStudents().stream()
                                    .map(s -> {
                                                Parent parent = student.getParent();
                                                return StudentDTO.builder()
                                                        .username(s.getUsername())
                                                        .profileName(s.getProfileName())
                                                        .email(s.getEmail())
                                                        .phone(s.getPhone())
                                                        .parentUsername(Objects.nonNull(parent) ? parent.getUsername() : null)
                                                        .parentEmail(Objects.nonNull(parent) ? parent.getEmail() : null)
                                                        .parentPhone(Objects.nonNull(parent) ? parent.getPhone() : null)
                                                        .build();
                                            }
                                    )
                                    .collect(Collectors.toSet()))
                            .build();
                } else {
                    throw new ResourceNotFoundException(NO_STUDENT_FOR_USERNAME);
                }
            } else {
                throw new AuthorizationException("Teacher not allow to add a student to the classroom");
            }

        } else {
            throw new ResourceNotFoundException(NO_CLASSROOM_FOR_ID);
        }
    }

    @Override
    public StudentSetResponseDTO dropStudent(String teacherUsername, String studentUsername, Long classroomId) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classroomId);
        if (optionalClassroom.isPresent()) {
            Classroom classroom = optionalClassroom.get();
            Teacher teacher = classroom.getTeacher();
            if (teacherUsername.equals(teacher.getUsername())) {
                Optional<Member> optionalStudent = userRepository.findByUsername(studentUsername);
                if (optionalStudent.isPresent()) {
                    Set<Student> students = classroom.getStudents();
                    students.removeIf(student -> studentUsername.equals(student.getUsername()));
                    classroom.setStudents(students);
                    classroomRepository.save(classroom);
                    return StudentSetResponseDTO.builder()
                            .students(classroom.getStudents().stream()
                                    .map(student -> {
                                        Parent parent = student.getParent();
                                        return StudentDTO.builder()
                                                .username(student.getUsername())
                                                .profileName(student.getProfileName())
                                                .email(student.getEmail())
                                                .phone(student.getPhone())
                                                .parentUsername(Objects.nonNull(parent) ? parent.getUsername() : null)
                                                .parentEmail(Objects.nonNull(parent) ? parent.getEmail() : null)
                                                .parentPhone(Objects.nonNull(parent) ? parent.getPhone() : null)
                                                .build();
                                    })
                                    .collect(Collectors.toSet()))
                            .build();
                } else {
                    throw new ResourceNotFoundException(NO_STUDENT_FOR_USERNAME);
                }
            } else {
                throw new AuthorizationException("Teacher not allow to drop a student from the classroom");
            }
        } else {
            throw new ResourceNotFoundException(NO_CLASSROOM_FOR_ID);
        }
    }

    @Override
    public StudentSetResponseDTO updateParent(String teacherUsername, String studentUsername, String parentUsername, Long classroomId) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classroomId);
        if (optionalClassroom.isPresent()) {
            Classroom classroom = optionalClassroom.get();
            Teacher teacher = classroom.getTeacher();
            if (teacherUsername.equals(teacher.getUsername())) {
                Optional<Member> optionalStudent = userRepository.findByUsername(studentUsername);
                if (optionalStudent.isPresent()) {
                    Student student = (Student) optionalStudent.get();
                    if (Objects.isNull(parentUsername)) {
                        student.setParent(null);
                    } else {
                        Optional<Member> optionalParent = userRepository.findByUsername(parentUsername);
                        if (optionalParent.isPresent()) {
                            Parent parent = (Parent) optionalParent.get();
                            student.setParent(parent);
                        } else {
                            throw new ResourceNotFoundException("No parent for given username");
                        }
                    }
                    userRepository.save(student);
                    return StudentSetResponseDTO.builder()
                            .students(classroom.getStudents().stream()
                                    .map(s -> {
                                        Parent parent = s.getParent();
                                        return StudentDTO.builder()
                                                .username(s.getUsername())
                                                .profileName(s.getProfileName())
                                                .email(s.getEmail())
                                                .phone(s.getPhone())
                                                .parentUsername(Objects.nonNull(parent) ? parent.getUsername() : null)
                                                .parentEmail(Objects.nonNull(parent) ? parent.getEmail() : null)
                                                .parentPhone(Objects.nonNull(parent) ? parent.getPhone() : null)
                                                .build();
                                    })
                                    .collect(Collectors.toSet()))
                            .build();
                } else {
                    throw new ResourceNotFoundException(NO_STUDENT_FOR_USERNAME);
                }
            } else {
                throw new AuthorizationException("Teacher not allow to add a parent to the student");
            }
        } else {
            throw new ResourceNotFoundException(NO_CLASSROOM_FOR_ID);
        }
    }

    @Override
    public ClassroomResponseDTO viewClassrooms(String username, String type) {
        Optional<Member> optionalMember = userRepository.findByUsername(username);
        if (optionalMember.isPresent()) {
            if (TEACHER.equals(type)) {
                Teacher teacher = (Teacher) optionalMember.get();
                return ClassroomResponseDTO.builder()
                        .classrooms(teacher.getClassrooms().stream()
                                .map(classroom -> ClassroomDTO.builder()
                                        .id(classroom.getId())
                                        .name(classroom.getName())
                                        .build())
                                .collect(Collectors.toSet()))
                        .build();
            } else {
                Student student = (Student) optionalMember.get();
                return ClassroomResponseDTO.builder()
                        .classrooms(student.getClassrooms().stream()
                                .map(classroom -> ClassroomDTO.builder()
                                        .id(classroom.getId())
                                        .name(classroom.getName())
                                        .build())
                                .collect(Collectors.toSet()))
                        .build();
            }
        } else {
            throw new ResourceNotFoundException("No user for given username");
        }
    }

    @Override
    public StudentSetResponseDTO viewStudents(String teacherUsername, Long classroomId) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classroomId);
        if (optionalClassroom.isPresent()) {
            Classroom classroom = optionalClassroom.get();
            Teacher teacher = classroom.getTeacher();
            if (teacherUsername.equals(teacher.getUsername())) {
                return StudentSetResponseDTO.builder()
                        .students(classroom.getStudents().stream()
                                .map(student -> {
                                    Parent parent = student.getParent();
                                    return StudentDTO.builder()
                                            .username(student.getUsername())
                                            .profileName(student.getProfileName())
                                            .email(student.getEmail())
                                            .phone(student.getPhone())
                                            .parentUsername(Objects.nonNull(parent) ? parent.getUsername() : null)
                                            .parentEmail(Objects.nonNull(parent) ? parent.getEmail() : null)
                                            .parentPhone(Objects.nonNull(parent) ? parent.getPhone() : null)
                                            .build();
                                })
                                .collect(Collectors.toSet()))
                        .build();
            } else {
                throw new AuthorizationException("Teacher not allow to add a student to the classroom");
            }

        } else {
            throw new ResourceNotFoundException(NO_CLASSROOM_FOR_ID);
        }
    }
}
