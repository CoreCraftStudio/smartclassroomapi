package com.smart.classroom.smartclassroom.service.impl;

import com.smart.classroom.smartclassroom.dto.*;
import com.smart.classroom.smartclassroom.entity.*;
import com.smart.classroom.smartclassroom.exception.AuthorizationException;
import com.smart.classroom.smartclassroom.exception.ResourceNotFoundException;
import com.smart.classroom.smartclassroom.repository.ClassroomRepository;
import com.smart.classroom.smartclassroom.repository.QuestionRepository;
import com.smart.classroom.smartclassroom.repository.QuizMarkRepository;
import com.smart.classroom.smartclassroom.repository.QuizRepository;
import com.smart.classroom.smartclassroom.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.smart.classroom.smartclassroom.util.Constant.QuestionAndAnswerTypeConstant.MULTIPLE_CHOICE;
import static com.smart.classroom.smartclassroom.util.Constant.QuestionAndAnswerTypeConstant.MULTIPLE_RESPONSE;
import static com.smart.classroom.smartclassroom.util.Constant.UserConstant.TEACHER;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final ClassroomRepository classroomRepository;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final QuizMarkRepository quizMarkRepository;

    @Override
    public QuizSetResponseDTO createQuiz(String teacherUsername, QuizRequestDTO quizRequestDTO) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(quizRequestDTO.getClassroomId());
        if (optionalClassroom.isPresent()) {
            Classroom classroom = optionalClassroom.get();
            if (teacherUsername.equals(classroom.getTeacher().getUsername())) {
                Quiz quiz = quizRepository.save(Quiz.builder()
                        .classroom(classroom)
                        .name(quizRequestDTO.getName())
                        .description(quizRequestDTO.getDescription())
                        .build());

                Set<Question> questions = new HashSet<>();
                for (QuestionDTO questionDTO : quizRequestDTO.getQuestions()) {
                    questions.add(questionRepository.save(switch (questionDTO.getType()) {
                                        case MULTIPLE_RESPONSE -> MultipleResponseQuestion.builder()
                                                .quiz(quiz)
                                                .description(questionDTO.getQuestion())
                                                .matchResponses(questionDTO.getMatchAnswers())
                                                .responses(questionDTO.getAnswers())
                                                .build();
                                        case MULTIPLE_CHOICE -> MultipleChoiceQuestion.builder()
                                                .quiz(quiz)
                                                .description(questionDTO.getQuestion())
                                                .matchChoice(questionDTO.getMatchAnswers().stream().findFirst().orElse(null))
                                                .choices(questionDTO.getAnswers())
                                                .build();
                                        default -> ShortAnswerQuestion.builder()
                                                .quiz(quiz)
                                                .description(questionDTO.getQuestion())
                                                .matchAnswer(questionDTO.getMatchAnswers().stream().findFirst().orElse(null))
                                                .build();
                                    }
                            )
                    );
                }

                quiz.setQuestions(questions);
                Set<Quiz> quizzes = classroom.getQuizzes();
                quizzes.add(quiz);
                classroomRepository.save(classroom);

                Set<QuizDTO> quizDTOs = quizzes.stream().map(
                                q -> QuizDTO.builder()
                                        .id(q.getId())
                                        .description(q.getDescription())
                                        .name(q.getName())
                                        .build()
                        )
                        .collect(Collectors.toSet());

                return QuizSetResponseDTO.builder()
                        .quizzes(quizDTOs)
                        .build();
            } else {
                throw new AuthorizationException("Teacher not allow to add a quiz to the classroom");
            }
        } else {
            throw new ResourceNotFoundException("No classroom for given id");
        }
    }

    @Override
    public QuizSetResponseDTO deleteQuiz(String teacherUsername, Long quizId) {
        Optional<Quiz> optionalQuiz = quizRepository.findById(quizId);
        if (optionalQuiz.isPresent()) {
            Quiz quiz = optionalQuiz.get();
            Classroom classroom = quiz.getClassroom();
            if (teacherUsername.equals(classroom.getTeacher().getUsername())) {
                quizRepository.deleteById(quizId);
                Set<Quiz> quizzes = classroom.getQuizzes();

                Set<QuizDTO> quizDTOs = quizzes.stream().map(
                                q -> QuizDTO.builder()
                                        .id(q.getId())
                                        .name(q.getName())
                                        .build()
                        )
                        .collect(Collectors.toSet());

                return QuizSetResponseDTO.builder()
                        .quizzes(quizDTOs)
                        .build();
            } else {
                throw new AuthorizationException("Teacher not allow to delete the quiz from the classroom");
            }
        } else {
            throw new ResourceNotFoundException("No quiz for given quiz id");
        }
    }


    @Override
    public QuizSetResponseDTO viewQuizzes(String username, String type, Long classroomId) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classroomId);
        if (optionalClassroom.isPresent()) {
            Classroom classroom = optionalClassroom.get();
            if (TEACHER.equals(type)) {
                if (username.equals(classroom.getTeacher().getUsername())) {
                    Set<Quiz> quizzes = classroom.getQuizzes();

                    Set<QuizDTO> quizDTOs = quizzes.stream().map(
                                    q -> QuizDTO.builder()
                                            .id(q.getId())
                                            .name(q.getName())
                                            .description(q.getDescription())
                                            .build()
                            )
                            .collect(Collectors.toSet());

                    return QuizSetResponseDTO.builder()
                            .quizzes(quizDTOs)
                            .build();
                } else {
                    throw new AuthorizationException("Teacher not allow to view the quiz of the classroom");
                }
            } else {
                if (classroom.getStudents().stream().map(Student::getUsername).collect(Collectors.toSet()).contains(username)) {
                    Set<Quiz> quizzes = classroom.getQuizzes();

                    Set<QuizDTO> quizDTOs = quizzes.stream().map(
                                    q -> QuizDTO.builder()
                                            .id(q.getId())
                                            .name(q.getName())
                                            .description(q.getDescription())
                                            .build()
                            )
                            .collect(Collectors.toSet());

                    return QuizSetResponseDTO.builder()
                            .quizzes(quizDTOs)
                            .build();
                } else {
                    throw new AuthorizationException("Student not allow to view the quiz of the classroom");
                }
            }

        } else {
            throw new ResourceNotFoundException("No classroom for given classroom id");
        }
    }

    @Override
    public QuizResponseDTO viewQuiz(String username, String type, Long quizId) {
        Optional<Quiz> optionalQuiz = quizRepository.findById(quizId);
        if (optionalQuiz.isPresent()) {
            Quiz quiz = optionalQuiz.get();
            if (TEACHER.equals(type)) {
                if (username.equals(quiz.getClassroom().getTeacher().getUsername())) {
                    QuizDTO quizDTO = QuizDTO.builder()
                            .id(quiz.getId())
                            .name(quiz.getName())
                            .description(quiz.getDescription())
                            .questions(quiz.getQuestions().stream().map(question -> {
                                        String questionType = questionRepository.findTypeById(question.getId());
                                        Set<String> answers;
                                        Set<String> matchAnswers;
                                        switch (questionType) {
                                            case MULTIPLE_RESPONSE -> {
                                                answers = ((MultipleResponseQuestion) question).getResponses();
                                                matchAnswers = ((MultipleResponseQuestion) question).getMatchResponses();

                                            }
                                            case MULTIPLE_CHOICE -> {
                                                answers = ((MultipleChoiceQuestion) question).getChoices();
                                                matchAnswers = Set.of(((MultipleChoiceQuestion) question).getMatchChoice());
                                            }
                                            default -> {
                                                answers = null;
                                                matchAnswers = Set.of(((ShortAnswerQuestion) question).getMatchAnswer());
                                            }
                                        }

                                        return QuestionDTO.builder()
                                                .id(question.getId())
                                                .question(question.getDescription())
                                                .type(questionType)
                                                .answers(answers)
                                                .matchAnswers(matchAnswers)
                                                .build();

                                    })
                                    .collect(Collectors.toSet()))
                            .build();

                    return QuizResponseDTO.builder()
                            .quiz(quizDTO)
                            .build();
                } else {
                    throw new AuthorizationException("Teacher not allow to delete the quiz from the classroom");
                }
            } else {
                Optional<QuizMark> quizMark = quizMarkRepository.findByUsernameAndQuizId(username, quizId);

                QuizDTO quizDTO = QuizDTO.builder()
                        .id(quiz.getId())
                        .name(quiz.getName())
                        .description(quiz.getDescription())
                        .questions(quiz.getQuestions().stream().map(question -> {
                                    Answer answer = question.getAnswers().stream()
                                            .filter(a -> username.equals(a.getStudent().getUsername()))
                                            .findFirst().orElse(null);

                                    String questionType = questionRepository.findTypeById(question.getId());
                                    Set<String> answers;
                                    Set<String> matchAnswers;
                                    Double mark = Objects.nonNull(answer) ? answer.getMark() : null;
                                    Set<String> selectedAnswers;
                                    switch (questionType) {
                                        case MULTIPLE_RESPONSE -> {
                                            answers = ((MultipleResponseQuestion) question).getResponses();
                                            matchAnswers = Objects.nonNull(mark) ? ((MultipleResponseQuestion) question).getMatchResponses() : null;
                                            selectedAnswers = Objects.nonNull(answer) ? ((MultipleResponseAnswer) answer).getResponses() : null;

                                        }
                                        case MULTIPLE_CHOICE -> {
                                            answers = ((MultipleChoiceQuestion) question).getChoices();
                                            matchAnswers = Objects.nonNull(mark) ? Set.of(((MultipleChoiceQuestion) question).getMatchChoice()) : null;
                                            selectedAnswers = Objects.nonNull(answer) ? Set.of(((MultipleChoiceAnswer) answer).getChoice()) : null;
                                        }
                                        default -> {
                                            answers = null;
                                            matchAnswers = Objects.nonNull(mark) ? Set.of(((ShortAnswerQuestion) question).getMatchAnswer()) : null;
                                            selectedAnswers = Objects.nonNull(answer) ? Set.of(((ShortAnswerAnswer) answer).getAnswer()) : null;
                                        }
                                    }

                                    return QuestionDTO.builder()
                                            .question(question.getDescription())
                                            .type(questionType)
                                            .answers(answers)
                                            .matchAnswers(matchAnswers)
                                            .selectedAnswers(selectedAnswers)
                                            .mark(mark)
                                            .build();

                                })
                                .collect(Collectors.toSet()))
                        .totalMark(quizMark.map(QuizMark::getTotalMark).orElse(null))
                        .maxMarks((double) quiz.getQuestions().size())
                        .build();

                return QuizResponseDTO.builder()
                        .quiz(quizDTO)
                        .build();
            }

        } else {
            throw new ResourceNotFoundException("No quiz for given quiz id");
        }

    }

}
