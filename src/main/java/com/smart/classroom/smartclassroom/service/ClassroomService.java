package com.smart.classroom.smartclassroom.service;

import com.smart.classroom.smartclassroom.dto.ClassroomRequestDTO;
import com.smart.classroom.smartclassroom.dto.ClassroomResponseDTO;
import com.smart.classroom.smartclassroom.dto.StudentSetResponseDTO;

public interface ClassroomService {

    ClassroomResponseDTO createClassroom(String teacherUsername, ClassroomRequestDTO classroomRequestDTO);

    ClassroomResponseDTO deleteClassroom(String teacherUsername, Long classroomId);

    StudentSetResponseDTO addStudent(String teacherUsername, String studentUsername, Long classroomId);

    StudentSetResponseDTO dropStudent(String teacherUsername, String studentUsername, Long classroomId);

    StudentSetResponseDTO updateParent(String teacherUsername, String studentUsername, String parentUsername, Long classroomId);

    ClassroomResponseDTO viewClassrooms(String username, String type);

    StudentSetResponseDTO viewStudents(String teacherUsername, Long classroomId);

}
