package com.smart.classroom.smartclassroom.service.impl;

import com.smart.classroom.smartclassroom.dto.AnswerRequestDTO;
import com.smart.classroom.smartclassroom.dto.AnswerSetRequestDTO;
import com.smart.classroom.smartclassroom.dto.QuizResponseDTO;
import com.smart.classroom.smartclassroom.entity.*;
import com.smart.classroom.smartclassroom.exception.ResourceNotFoundException;
import com.smart.classroom.smartclassroom.repository.*;
import com.smart.classroom.smartclassroom.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.smart.classroom.smartclassroom.util.Constant.QuestionAndAnswerTypeConstant.MULTIPLE_CHOICE;
import static com.smart.classroom.smartclassroom.util.Constant.QuestionAndAnswerTypeConstant.MULTIPLE_RESPONSE;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuizRepository quizRepository;
    private final QuizMarkRepository quizMarkRepository;

    @Override
    public QuizResponseDTO createAnswerSet(String studentUsername, AnswerSetRequestDTO answerSetRequestDTO) {
        Optional<Member> optionalStudent = userRepository.findByUsername(studentUsername);
        Optional<Quiz> optionalQuiz = quizRepository.findById(answerSetRequestDTO.getQuizId());
        if (optionalStudent.isPresent() && optionalQuiz.isPresent()) {
            Student student = (Student) optionalStudent.get();
            Quiz quiz = optionalQuiz.get();
            Set<Long> questionIds = quiz.getQuestions().stream().map(Question::getId).collect(Collectors.toSet());
            Set<Answer> answers = new HashSet<>();
            for (AnswerRequestDTO answerRequestDTO : answerSetRequestDTO.getAnswerSet()) {
                Optional<Question> optionalQuestion = questionRepository.findById(answerRequestDTO.getQuestionId());
                if (optionalQuestion.isPresent() && questionIds.contains(answerRequestDTO.getQuestionId())) {
                    String questionType = questionRepository.findTypeById(answerRequestDTO.getQuestionId());
                    answers.add(
                            switch (questionType) {
                                case MULTIPLE_RESPONSE -> {
                                    MultipleResponseQuestion question = (MultipleResponseQuestion) optionalQuestion.get();
                                    Double mark = calculateQuestionMark(question.getMatchResponses(), answerRequestDTO.getAnswers());
                                    yield MultipleResponseAnswer.builder()
                                            .mark(mark)
                                            .student(student)
                                            .question(question)
                                            .responses(answerRequestDTO.getAnswers())
                                            .build();
                                }
                                case MULTIPLE_CHOICE -> {
                                    MultipleChoiceQuestion question = (MultipleChoiceQuestion) optionalQuestion.get();
                                    Double mark = calculateQuestionMark(Set.of(question.getMatchChoice()), answerRequestDTO.getAnswers());
                                    yield MultipleChoiceAnswer.builder()
                                            .mark(mark)
                                            .student(student)
                                            .question(question)
                                            .choice(answerRequestDTO.getAnswers().stream().findFirst().orElse(null))
                                            .build();
                                }
                                default -> {
                                    ShortAnswerQuestion question = (ShortAnswerQuestion) optionalQuestion.get();
                                    Double mark = calculateQuestionMark(Set.of(question.getMatchAnswer()), answerRequestDTO.getAnswers());
                                    yield ShortAnswerAnswer.builder()
                                            .mark(mark)
                                            .student(student)
                                            .question(question)
                                            .answer(answerRequestDTO.getAnswers().stream().findFirst().orElse(null))
                                            .build();
                                }
                            }
                    );
                } else {
                    throw new ResourceNotFoundException("No question in given quiz for given id");
                }
            }
            Double totalMark = answers.stream().mapToDouble(Answer::getMark).sum();
            quizMarkRepository.save(QuizMark.builder()
                    .student(student)
                    .totalMark(totalMark)
                    .quiz(quiz)
                    .build());

            answerRepository.saveAll(answers);
            Classroom classroom = quiz.getClassroom();
            Set<Quiz> quizzes = classroom.getQuizzes();
            quizzes.stream()
                    .map(Quiz::getQuestions)
                    .flatMap(Set::stream)
                    .map(Question::getAnswers)
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet())
                    .removeIf(answer -> !studentUsername.equals(answer.getStudent().getUsername()));

            return QuizResponseDTO.builder()
                    .quizzes(quizzes)
                    .build();
        } else {
            throw new ResourceNotFoundException("No quiz for given id");
        }

    }


    private Double calculateQuestionMark(Set<String> matchAnswers, Set<String> answers) {
        AtomicInteger matched = new AtomicInteger();
        answers.forEach(answer -> {
            if (matchAnswers.contains(answer)) {
                matched.getAndIncrement();
            }
        });
        return matched.doubleValue() / matchAnswers.size();
    }

}
