package com.smart.classroom.smartclassroom.contoller;


import com.smart.classroom.smartclassroom.dto.ClassroomRequestDTO;
import com.smart.classroom.smartclassroom.dto.ClassroomResponseDTO;
import com.smart.classroom.smartclassroom.dto.StudentResponseDTO;
import com.smart.classroom.smartclassroom.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ClassroomController {

    private final ClassroomService classroomService;

    @PostMapping("/classroom")
    public ClassroomResponseDTO setClassroom(@RequestBody ClassroomRequestDTO classroomRequestDTO) {
        return classroomService.createClassroom(classroomRequestDTO);
    }

    @DeleteMapping("/classroom/{classroomId}/teacher/{teacherEmail}")
    public ClassroomResponseDTO deleteClassroom(@PathVariable String teacherEmail, @PathVariable Long classroomId) {
        return classroomService.deleteClassroom(teacherEmail, classroomId);
    }


    @PutMapping("/classroom/{classroomId}/teacher/{teacherEmail}/student/{studentEmail}")
    public StudentResponseDTO addStudent(@PathVariable String teacherEmail, @PathVariable String studentEmail, @PathVariable Long classroomId) {
        return classroomService.addStudent(teacherEmail, studentEmail, classroomId);
    }

    @DeleteMapping("/classroom/{classroomId}/teacher/{teacherEmail}/student/{studentEmail}")
    public StudentResponseDTO dropStudent(@PathVariable String teacherEmail, @PathVariable String studentEmail, @PathVariable Long classroomId) {
        return classroomService.dropStudent(teacherEmail, studentEmail, classroomId);
    }

    @PatchMapping("/classroom/{classroomId}/teacher/{teacherEmail}/student/{studentEmail}")
    public StudentResponseDTO updateParent(@PathVariable String teacherEmail, @PathVariable String studentEmail, @RequestParam(required = false) String parentEmail, @PathVariable Long classroomId) {
        return classroomService.updateParent(teacherEmail, studentEmail, parentEmail, classroomId);
    }

}
