package com.smart.classroom.smartclassroom.contoller;

import com.smart.classroom.smartclassroom.dto.AssignmentRequestDTO;
import com.smart.classroom.smartclassroom.dto.AssignmentResponseDTO;
import com.smart.classroom.smartclassroom.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping("/assignments")
    public AssignmentResponseDTO createAssignment(@RequestBody AssignmentRequestDTO assignmentRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return assignmentService.createAssignment(user.getUsername(), assignmentRequestDTO);
    }

    @DeleteMapping("/assignments")
    public AssignmentResponseDTO deleteAssignment(@RequestParam Long classroomId, @RequestParam Long assignmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return assignmentService.deleteAssignment(user.getUsername(), classroomId, assignmentId);
    }

    @GetMapping("/assignments")
    public AssignmentResponseDTO viewAssignment(@RequestParam Long classroomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Optional<GrantedAuthority> optionalAuthority = user.getAuthorities().stream().findFirst();
        String type = optionalAuthority.map(GrantedAuthority::getAuthority).orElse(null);
        return assignmentService.viewAssignments(user.getUsername(), type, classroomId);
    }

}
