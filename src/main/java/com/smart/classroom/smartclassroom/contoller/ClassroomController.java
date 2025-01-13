package com.smart.classroom.smartclassroom.contoller;


import com.smart.classroom.smartclassroom.dto.ClassroomRequestDTO;
import com.smart.classroom.smartclassroom.dto.ClassroomResponseDTO;
import com.smart.classroom.smartclassroom.dto.QuizMarkResponseDTO;
import com.smart.classroom.smartclassroom.dto.StudentResponseDTO;
import com.smart.classroom.smartclassroom.service.ClassroomService;
import com.smart.classroom.smartclassroom.service.MarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ClassroomController {

    private final ClassroomService classroomService;
    private final MarkService markService;

    @PostMapping("/classrooms")
    public ClassroomResponseDTO createClassroom(@RequestBody ClassroomRequestDTO classroomRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return classroomService.createClassroom(user.getUsername(), classroomRequestDTO);
    }

    @DeleteMapping("/classrooms")
    public ClassroomResponseDTO deleteClassroom(@RequestParam Long classroomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return classroomService.deleteClassroom(user.getUsername(), classroomId);
    }


    @PutMapping("/classrooms/{classroomId}")
    public StudentResponseDTO addStudent(@RequestParam String studentUsername, @PathVariable Long classroomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return classroomService.addStudent(user.getUsername(), studentUsername, classroomId);
    }

    @DeleteMapping("/classrooms/{classroomId}")
    public StudentResponseDTO dropStudent(@RequestParam String studentUsername, @PathVariable Long classroomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return classroomService.dropStudent(user.getUsername(), studentUsername, classroomId);
    }

    @PatchMapping("/classrooms/{classroomId}")
    public StudentResponseDTO updateParent(@RequestParam String studentUsername, @RequestParam(required = false) String parentUsername, @PathVariable Long classroomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return classroomService.updateParent(user.getUsername(), studentUsername, parentUsername, classroomId);
    }

    @GetMapping("/classrooms")
    public ClassroomResponseDTO viewClassrooms() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Optional<GrantedAuthority> optionalAuthority = user.getAuthorities().stream().findFirst();
        String type = optionalAuthority.map(GrantedAuthority::getAuthority).orElse(null);
        return classroomService.viewClassrooms(user.getUsername(), type);
    }

    @GetMapping("/classrooms/{classroomId}")
    public StudentResponseDTO viewStudents(@PathVariable Long classroomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return classroomService.viewStudents(user.getUsername(), classroomId);
    }

    @GetMapping("/classrooms/{classroomId}/quiz")
    public QuizMarkResponseDTO viewMarks(@PathVariable Long classroomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return markService.getQuizMarks(user.getUsername(), classroomId);
    }

}
