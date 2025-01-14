package com.smart.classroom.smartclassroom.contoller;

import com.smart.classroom.smartclassroom.dto.QuizReportResponseDTO;
import com.smart.classroom.smartclassroom.service.MarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final MarkService markService;

    @GetMapping("/quiz-report")
    public QuizReportResponseDTO viewQuizReport(@RequestParam Long quizId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return markService.getQuizMarks(user.getUsername(), quizId);
    }

}
