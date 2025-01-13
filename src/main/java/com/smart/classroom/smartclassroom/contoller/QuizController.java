package com.smart.classroom.smartclassroom.contoller;

import com.smart.classroom.smartclassroom.dto.*;
import com.smart.classroom.smartclassroom.service.AnswerService;
import com.smart.classroom.smartclassroom.service.MarkService;
import com.smart.classroom.smartclassroom.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;
    private final AnswerService answerService;
    private final MarkService markService;

    @PostMapping("/quizzes")
    public QuizSetResponseDTO createQuiz(@RequestBody QuizRequestDTO quizRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return quizService.createQuiz(user.getUsername(), quizRequestDTO);
    }

    @DeleteMapping("/quizzes")
    public QuizSetResponseDTO deleteQuiz(@RequestParam Long quizId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return quizService.deleteQuiz(user.getUsername(), quizId);
    }

    @PutMapping("/quizzes")
    public QuizSetResponseDTO createAnswer(@RequestBody AnswerSetRequestDTO answerSetRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return answerService.createAnswerSet(user.getUsername(), answerSetRequestDTO);
    }

    @GetMapping("/quizzes")
    public QuizSetResponseDTO viewQuizzes(@RequestParam Long classroomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Optional<GrantedAuthority> optionalAuthority = user.getAuthorities().stream().findFirst();
        String type = optionalAuthority.map(GrantedAuthority::getAuthority).orElse(null);
        return quizService.viewQuizzes(user.getUsername(), type, classroomId);
    }

    @GetMapping("/quizzes/{quizId}")
    public QuizResponseDTO viewQuiz(@PathVariable Long quizId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Optional<GrantedAuthority> optionalAuthority = user.getAuthorities().stream().findFirst();
        String type = optionalAuthority.map(GrantedAuthority::getAuthority).orElse(null);
        return quizService.viewQuiz(user.getUsername(), type, quizId);
    }

    @GetMapping("/quizzes/{quizId}/quiz-marks")
    public StudentQuizMarkResponseDTO viewMarks(@PathVariable Long quizId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return markService.getStudentQuizMarks(user.getUsername(), quizId);
    }

}
