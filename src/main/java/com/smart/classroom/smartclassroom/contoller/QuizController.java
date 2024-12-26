package com.smart.classroom.smartclassroom.contoller;

import com.smart.classroom.smartclassroom.dto.AnswerSetRequestDTO;
import com.smart.classroom.smartclassroom.dto.QuizRequestDTO;
import com.smart.classroom.smartclassroom.dto.QuizResponseDTO;
import com.smart.classroom.smartclassroom.service.AnswerService;
import com.smart.classroom.smartclassroom.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;
    private final AnswerService answerService;

    @PostMapping("/quiz")
    public QuizResponseDTO createQuiz(@RequestBody QuizRequestDTO quizRequestDTO) {
        return quizService.createQuiz(quizRequestDTO);
    }

    @DeleteMapping("/quiz/{quizId}/classroom/{classroomId}/teacher/{teacherEmail}")
    public QuizResponseDTO deleteQuiz(@PathVariable Long quizId, @PathVariable Long classroomId, @PathVariable String teacherEmail) {
        return quizService.deleteQuiz(teacherEmail, classroomId, quizId);
    }

    @PutMapping("/quiz")
    public QuizResponseDTO createAnswer(@RequestBody AnswerSetRequestDTO answerSetRequestDTO) {
        return answerService.createAnswerSet(answerSetRequestDTO);
    }

}
