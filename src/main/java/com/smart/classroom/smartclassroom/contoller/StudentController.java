package com.smart.classroom.smartclassroom.contoller;

import com.smart.classroom.smartclassroom.dto.StudentSetResponseDTO;
import com.smart.classroom.smartclassroom.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StudentController {

    private final ClassroomService classroomService;

    @PatchMapping("/students/{student-id}")
    public StudentSetResponseDTO updateParent(@PathVariable("student-id") String studentUsername, @RequestParam(required = false) String parentUsername, @RequestParam Long classroomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return classroomService.updateParent(user.getUsername(), studentUsername, parentUsername, classroomId);
    }
}
