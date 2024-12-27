package com.smart.classroom.smartclassroom.service;

import com.smart.classroom.smartclassroom.dto.ClassroomRequestDTO;
import com.smart.classroom.smartclassroom.dto.ClassroomResponseDTO;
import com.smart.classroom.smartclassroom.dto.StudentResponseDTO;

public interface ClassroomService {

    ClassroomResponseDTO createClassroom(String teacherEmail, ClassroomRequestDTO classroomRequestDTO);

    ClassroomResponseDTO deleteClassroom(String teacherEmail, Long classroomId);

    StudentResponseDTO addStudent(String teacherEmail, String studentEmail, Long classroomId);

    StudentResponseDTO dropStudent(String teacherEmail, String studentEmail, Long classroomId);

    StudentResponseDTO updateParent(String teacherEmail, String studentEmail, String parentEmail, Long classroomId);

}
