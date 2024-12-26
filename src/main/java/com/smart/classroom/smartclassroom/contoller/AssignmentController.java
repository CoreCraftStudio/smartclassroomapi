package com.smart.classroom.smartclassroom.contoller;

import com.smart.classroom.smartclassroom.dto.AssignmentRequestDTO;
import com.smart.classroom.smartclassroom.dto.AssignmentResponseDTO;
import com.smart.classroom.smartclassroom.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping("/assignment")
    public AssignmentResponseDTO createAssignment(@RequestBody AssignmentRequestDTO assignmentRequestDTO) {
        return assignmentService.createAssignment(assignmentRequestDTO);
    }

    @DeleteMapping("/assignment")
    public AssignmentResponseDTO deleteAssignment(@RequestParam String email, @RequestParam Long classroomId, @RequestParam Long assignmentId) {
        return assignmentService.deleteAssignment(email, classroomId, assignmentId);
    }

    @GetMapping("/assignment/teacher/{teacherEmail}")
    public AssignmentResponseDTO viewAssignmentByTeacher(@PathVariable String teacherEmail, @RequestParam Long classroomId, @RequestParam Long assignmentId) {
        return assignmentService.viewAssignmentByTeacher(teacherEmail, classroomId, assignmentId);
    }

    @GetMapping("/assignment/{studentEmail}")
    public AssignmentResponseDTO viewAssignmentByStudent(@PathVariable String studentEmail, @RequestParam Long classroomId, @RequestParam Long assignmentId) {
        return assignmentService.viewAssignmentByStudent(studentEmail, classroomId, assignmentId);
    }

}
