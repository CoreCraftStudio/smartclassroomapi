package com.smart.classroom.smartclassroom.service;

import com.smart.classroom.smartclassroom.dto.ClassroomRequestDTO;
import com.smart.classroom.smartclassroom.dto.ClassroomResponseDTO;
import com.smart.classroom.smartclassroom.dto.StudentResponseDTO;

public interface ClassroomService {

    ClassroomResponseDTO createClassroom(String teacherUsername, ClassroomRequestDTO classroomRequestDTO);

    ClassroomResponseDTO deleteClassroom(String teacherUsername, Long classroomId);

    StudentResponseDTO addStudent(String teacherUsername, String studentUsername, Long classroomId);

    StudentResponseDTO dropStudent(String teacherUsername, String studentUsername, Long classroomId);

    StudentResponseDTO updateParent(String teacherUsername, String studentUsername, String parentUsername, Long classroomId);

    ClassroomResponseDTO viewClassrooms(String username, String type);

}
