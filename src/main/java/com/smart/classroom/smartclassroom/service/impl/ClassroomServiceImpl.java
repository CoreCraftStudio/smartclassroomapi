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
                    .classrooms(teacher.getClassrooms())
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
    public StudentResponseDTO addStudent(String teacherUsername, String studentUsername, Long classroomId) {
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
                    return StudentResponseDTO.builder()
                            .students(classroom.getStudents())
                            .build();
                } else {
                    throw new ResourceNotFoundException("No student for given username");
                }
            } else {
                throw new AuthorizationException("Teacher not allow to add a student to the classroom");
            }

        } else {
            throw new ResourceNotFoundException("No classroom for given classroom id");
        }
    }

    @Override
    public StudentResponseDTO dropStudent(String teacherUsername, String studentUsername, Long classroomId) {
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
                    return StudentResponseDTO.builder()
                            .students(classroom.getStudents())
                            .build();
                } else {
                    throw new ResourceNotFoundException("No student for given username");
                }
            } else {
                throw new AuthorizationException("Teacher not allow to drop a student from the classroom");
            }
        } else {
            throw new ResourceNotFoundException("No classroom for given classroom id");
        }
    }

    @Override
    public StudentResponseDTO updateParent(String teacherUsername, String studentUsername, String parentUsername, Long classroomId) {
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
                    return StudentResponseDTO.builder()
                            .students(classroom.getStudents())
                            .build();
                } else {
                    throw new ResourceNotFoundException("No student for given username");
                }
            } else {
                throw new AuthorizationException("Teacher not allow to add a parent to the student");
            }
        } else {
            throw new ResourceNotFoundException("No classroom for given classroom id");
        }
    }

    @Override
    public ClassroomResponseDTO viewClassrooms(String username, String type) {
        Optional<Member> optionalMember = userRepository.findByUsername(username);
        if (optionalMember.isPresent()) {
            if (TEACHER.equals(type)) {
                Teacher teacher = (Teacher) optionalMember.get();
                return ClassroomResponseDTO.builder()
                        .classrooms(teacher.getClassrooms())
                        .build();
            } else {
                Student student = (Student) optionalMember.get();
                return ClassroomResponseDTO.builder()
                        .classrooms(student.getClassrooms())
                        .build();
            }
        } else {
            throw new ResourceNotFoundException("No user for given username");
        }
    }
}
