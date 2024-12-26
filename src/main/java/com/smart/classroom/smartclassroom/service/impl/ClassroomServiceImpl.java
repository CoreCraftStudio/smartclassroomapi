package com.smart.classroom.smartclassroom.service.impl;

import com.smart.classroom.smartclassroom.dto.ClassroomRequestDTO;
import com.smart.classroom.smartclassroom.dto.ClassroomResponseDTO;
import com.smart.classroom.smartclassroom.dto.StudentResponseDTO;
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

@Service
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;

    @Override
    public ClassroomResponseDTO createClassroom(ClassroomRequestDTO classroomRequestDTO) {
        Optional<User> optionalTeacher = userRepository.findByEmail(classroomRequestDTO.getEmail());
        if (optionalTeacher.isPresent()) {
            Teacher teacher = (Teacher) optionalTeacher.get();
            classroomRepository.save(Classroom.builder()
                    .teacher(teacher)
                    .name(classroomRequestDTO.getName())
                    .build());

            return ClassroomResponseDTO.builder()
                    .classrooms(teacher.getClassrooms())
                    .build();
        } else {
            throw new ResourceNotFoundException("No teacher for given email");
        }
    }

    @Override
    public ClassroomResponseDTO deleteClassroom(String teacherEmail, Long classroomId) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classroomId);
        if (optionalClassroom.isPresent()) {
            Classroom classroom = optionalClassroom.get();
            Teacher teacher = classroom.getTeacher();
            if (teacherEmail.equals(teacher.getEmail())) {
                classroomRepository.deleteById(classroomId);
                return ClassroomResponseDTO.builder()
                        .classrooms(teacher.getClassrooms())
                        .build();
            } else {
                throw new AuthorizationException("Teacher not allow to delete the classroom");
            }
        } else {
            throw new ResourceNotFoundException("No classroom for given classroom id");
        }
    }

    @Override
    public StudentResponseDTO addStudent(String teacherEmail, String studentEmail, Long classroomId) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classroomId);
        if (optionalClassroom.isPresent()) {
            Classroom classroom = optionalClassroom.get();
            Teacher teacher = classroom.getTeacher();
            if (teacherEmail.equals(teacher.getEmail())) {
                Optional<User> optionalStudent = userRepository.findByEmail(studentEmail);
                if (optionalStudent.isPresent()) {
                    Student student = (Student) optionalStudent.get();
                    Set<Student> students = classroom.getStudents();
                    students.add(student);
                    classroom.setStudents(students);
                    classroomRepository.save(classroom);
                    return StudentResponseDTO.builder()
                            .students(classroom.getStudents())
                            .build();
                } else {
                    throw new ResourceNotFoundException("No student for given email");
                }
            } else {
                throw new AuthorizationException("Teacher not allow to add a student to the classroom");
            }

        } else {
            throw new ResourceNotFoundException("No classroom for given classroom id");
        }
    }

    @Override
    public StudentResponseDTO dropStudent(String teacherEmail, String studentEmail, Long classroomId) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classroomId);
        if (optionalClassroom.isPresent()) {
            Classroom classroom = optionalClassroom.get();
            Teacher teacher = classroom.getTeacher();
            if (teacherEmail.equals(teacher.getEmail())) {
                Optional<User> optionalStudent = userRepository.findByEmail(studentEmail);
                if (optionalStudent.isPresent()) {
                    Set<Student> students = classroom.getStudents();
                    students.removeIf(student -> studentEmail.equals(student.getEmail()));
                    classroom.setStudents(students);
                    classroomRepository.save(classroom);
                    return StudentResponseDTO.builder()
                            .students(classroom.getStudents())
                            .build();
                } else {
                    throw new ResourceNotFoundException("No student for given email");
                }
            } else {
                throw new AuthorizationException("Teacher not allow to drop a student from the classroom");
            }
        } else {
            throw new ResourceNotFoundException("No classroom for given classroom id");
        }
    }

    @Override
    public StudentResponseDTO updateParent(String teacherEmail, String studentEmail, String parentEmail, Long classroomId) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classroomId);
        if (optionalClassroom.isPresent()) {
            Classroom classroom = optionalClassroom.get();
            Teacher teacher = classroom.getTeacher();
            if (teacherEmail.equals(teacher.getEmail())) {
                Optional<User> optionalStudent = userRepository.findByEmail(studentEmail);
                if (optionalStudent.isPresent()) {
                    Student student = (Student) optionalStudent.get();
                    if (Objects.isNull(parentEmail)) {
                        student.setParent(null);
                    } else {
                        Optional<User> optionalParent = userRepository.findByEmail(parentEmail);
                        if (optionalParent.isPresent()) {
                            Parent parent = (Parent) optionalParent.get();
                            student.setParent(parent);
                        } else {
                            throw new ResourceNotFoundException("No parent for given email");
                        }
                    }
                    userRepository.save(student);
                    return StudentResponseDTO.builder()
                            .students(classroom.getStudents())
                            .build();
                } else {
                    throw new ResourceNotFoundException("No student for given email");
                }
            } else {
                throw new AuthorizationException("Teacher not allow to add a parent to the student");
            }
        } else {
            throw new ResourceNotFoundException("No classroom for given classroom id");
        }
    }
}
