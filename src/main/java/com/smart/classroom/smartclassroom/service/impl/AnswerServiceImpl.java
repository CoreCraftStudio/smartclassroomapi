package com.smart.classroom.smartclassroom.service.impl;

import com.smart.classroom.smartclassroom.dto.*;
import com.smart.classroom.smartclassroom.entity.*;
import com.smart.classroom.smartclassroom.exception.ResourceNotFoundException;
import com.smart.classroom.smartclassroom.repository.*;
import com.smart.classroom.smartclassroom.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
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
            QuizMark quizMark = quizMarkRepository.save(QuizMark.builder()
                    .student(student)
                    .totalMark(totalMark)
                    .quiz(quiz)
                    .build());

            answerRepository.saveAll(answers);

            QuizDTO quizDTO = QuizDTO.builder()
                    .id(quiz.getId())
                    .name(quiz.getName())
                    .totalMark(totalMark)
                    .maxMarks((double) quiz.getQuestions().size())
                    .questions(quiz.getQuestions().stream().map(question -> {
                                Answer answer = question.getAnswers().stream()
                                        .filter(a -> studentUsername.equals(a.getStudent().getUsername()))
                                        .findFirst().orElse(null);

                                String questionType = questionRepository.findTypeById(question.getId());
                                Set<String> ansSet;
                                Set<String> matchAnswers;
                                Double mark = Objects.nonNull(answer) ? answer.getMark() : null;
                                Set<String> selectedAnswers;
                                switch (questionType) {
                                    case MULTIPLE_RESPONSE -> {
                                        ansSet = ((MultipleResponseQuestion) question).getResponses();
                                        matchAnswers = Objects.nonNull(mark) ? ((MultipleResponseQuestion) question).getMatchResponses() : null;
                                        selectedAnswers = Objects.nonNull(answer) ? ((MultipleResponseAnswer) answer).getResponses() : null;

                                    }
                                    case MULTIPLE_CHOICE -> {
                                        ansSet = ((MultipleChoiceQuestion) question).getChoices();
                                        matchAnswers = Objects.nonNull(mark) ? Set.of(((MultipleChoiceQuestion) question).getMatchChoice()) : null;
                                        selectedAnswers = Objects.nonNull(answer) ? Set.of(((MultipleChoiceAnswer) answer).getChoice()) : null;
                                    }
                                    default -> {
                                        ansSet = null;
                                        matchAnswers = Objects.nonNull(mark) ? Set.of(((ShortAnswerQuestion) question).getMatchAnswer()) : null;
                                        selectedAnswers = Objects.nonNull(answer) ? Set.of(((ShortAnswerAnswer) answer).getAnswer()) : null;
                                    }
                                }

                                return QuestionDTO.builder()
                                        .id(question.getId())
                                        .question(question.getDescription())
                                        .type(questionType)
                                        .answers(ansSet)
                                        .matchAnswers(matchAnswers)
                                        .selectedAnswers(selectedAnswers)
                                        .mark(mark)
                                        .build();

                            })
                            .collect(Collectors.toSet()))
                    .totalMark(quizMark.getTotalMark())
                    .maxMarks((double) quiz.getQuestions().size())
                    .build();

            return QuizResponseDTO.builder()
                    .quiz(quizDTO)
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
