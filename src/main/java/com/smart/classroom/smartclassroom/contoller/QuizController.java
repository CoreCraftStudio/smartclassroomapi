package com.smart.classroom.smartclassroom.contoller;

import com.smart.classroom.smartclassroom.dto.AnswerSetRequestDTO;
import com.smart.classroom.smartclassroom.dto.QuizRequestDTO;
import com.smart.classroom.smartclassroom.dto.QuizResponseDTO;
import com.smart.classroom.smartclassroom.service.AnswerService;
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

    @PostMapping("/quizzes")
    public QuizResponseDTO createQuiz(@RequestBody QuizRequestDTO quizRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return quizService.createQuiz(user.getUsername(), quizRequestDTO);
    }

    @DeleteMapping("/quizzes")
    public QuizResponseDTO deleteQuiz(@RequestParam Long quizId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return quizService.deleteQuiz(user.getUsername(), quizId);
    }

    @PutMapping("/quizzes")
    public QuizResponseDTO createAnswer(@RequestBody AnswerSetRequestDTO answerSetRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return answerService.createAnswerSet(user.getUsername(), answerSetRequestDTO);
    }

    @GetMapping("/quizzes")
    public QuizResponseDTO getQuizzes(@RequestParam Long classroomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Optional<GrantedAuthority> optionalAuthority = user.getAuthorities().stream().findFirst();
        String type = optionalAuthority.map(GrantedAuthority::getAuthority).orElse(null);
        return quizService.viewQuizzes(user.getUsername(), type, classroomId);
    }

}
