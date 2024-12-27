package com.smart.classroom.smartclassroom.contoller;


import com.smart.classroom.smartclassroom.dto.ClassroomRequestDTO;
import com.smart.classroom.smartclassroom.dto.ClassroomResponseDTO;
import com.smart.classroom.smartclassroom.dto.StudentResponseDTO;
import com.smart.classroom.smartclassroom.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ClassroomController {

    private final ClassroomService classroomService;

    @PostMapping("/classroom")
    public ClassroomResponseDTO setClassroom(@RequestBody ClassroomRequestDTO classroomRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return classroomService.createClassroom(user.getUsername(), classroomRequestDTO);
    }

    @DeleteMapping("/classroom/{classroomId}")
    public ClassroomResponseDTO deleteClassroom(@PathVariable Long classroomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return classroomService.deleteClassroom(user.getUsername(), classroomId);
    }


    @PutMapping("/classroom/{classroomId}/student/{studentEmail}")
    public StudentResponseDTO addStudent(@PathVariable String studentEmail, @PathVariable Long classroomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return classroomService.addStudent(user.getUsername(), studentEmail, classroomId);
    }

    @DeleteMapping("/classroom/{classroomId}/student/{studentEmail}")
    public StudentResponseDTO dropStudent(@PathVariable String studentEmail, @PathVariable Long classroomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return classroomService.dropStudent(user.getUsername(), studentEmail, classroomId);
    }

    @PatchMapping("/classroom/{classroomId}/student/{studentEmail}")
    public StudentResponseDTO updateParent(@PathVariable String studentEmail, @RequestParam(required = false) String parentEmail, @PathVariable Long classroomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return classroomService.updateParent(user.getUsername(), studentEmail, parentEmail, classroomId);
    }

}
