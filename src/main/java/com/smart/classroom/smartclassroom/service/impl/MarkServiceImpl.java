package com.smart.classroom.smartclassroom.service.impl;

import com.smart.classroom.smartclassroom.dto.QuizReportResponseDTO;
import com.smart.classroom.smartclassroom.dto.StudentQuizMarkDTO;
import com.smart.classroom.smartclassroom.entity.Quiz;
import com.smart.classroom.smartclassroom.entity.QuizMark;
import com.smart.classroom.smartclassroom.exception.AuthorizationException;
import com.smart.classroom.smartclassroom.exception.ResourceNotFoundException;
import com.smart.classroom.smartclassroom.repository.QuizMarkRepository;
import com.smart.classroom.smartclassroom.repository.QuizRepository;
import com.smart.classroom.smartclassroom.service.MarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarkServiceImpl implements MarkService {

    private final QuizMarkRepository quizMarkRepository;
    private final QuizRepository quizRepository;

    @Override
    public QuizReportResponseDTO getQuizMarks(String teacherUsername, Long quizId) {
        Optional<Quiz> optionalQuiz = quizRepository.findById(quizId);
        if (optionalQuiz.isPresent()) {
            Quiz quiz = optionalQuiz.get();
            if (teacherUsername.equals(quiz.getClassroom().getTeacher().getUsername())) {
                Set<QuizMark> quizMarks = quizMarkRepository.findByQuizId(quizId);
                Set<StudentQuizMarkDTO> studentQuizMarks = quizMarks.stream()
                        .map(quizMark -> StudentQuizMarkDTO.builder()
                                .studentUsername(quizMark.getStudent().getUsername())
                                .totalMarks(quizMark.getTotalMark())
                                .build()).collect(Collectors.toSet());

                return QuizReportResponseDTO.builder()
                        .studentQuizMarks(studentQuizMarks)
                        .build();
            } else {
                throw new AuthorizationException("Teacher not allow to view the quiz marks for given id");
            }
        } else {
            throw new ResourceNotFoundException("No quiz for given id");
        }
    }
}
