package com.smart.classroom.smartclassroom.service.impl;

import com.smart.classroom.smartclassroom.dto.QuestionRequestDTO;
import com.smart.classroom.smartclassroom.dto.QuizRequestDTO;
import com.smart.classroom.smartclassroom.dto.QuizResponseDTO;
import com.smart.classroom.smartclassroom.entity.*;
import com.smart.classroom.smartclassroom.exception.AuthorizationException;
import com.smart.classroom.smartclassroom.exception.ResourceNotFoundException;
import com.smart.classroom.smartclassroom.repository.ClassroomRepository;
import com.smart.classroom.smartclassroom.repository.QuestionRepository;
import com.smart.classroom.smartclassroom.repository.QuizRepository;
import com.smart.classroom.smartclassroom.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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

    @Override
    public QuizResponseDTO createQuiz(String teacherUsername, QuizRequestDTO quizRequestDTO) {
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
                for (QuestionRequestDTO questionRequestDTO : quizRequestDTO.getQuestionRequestDTOs()) {
                    questions.add(questionRepository.save(switch (questionRequestDTO.getQuestionType()) {
                                        case MULTIPLE_RESPONSE -> MultipleResponseQuestion.builder()
                                                .quiz(quiz)
                                                .description(quizRequestDTO.getDescription())
                                                .matchResponses(questionRequestDTO.getMatchAnswers())
                                                .responses(questionRequestDTO.getAnswers())
                                                .build();
                                        case MULTIPLE_CHOICE -> MultipleChoiceQuestion.builder()
                                                .quiz(quiz)
                                                .description(quizRequestDTO.getDescription())
                                                .matchChoice(questionRequestDTO.getMatchAnswers().stream().findFirst().orElse(null))
                                                .choices(questionRequestDTO.getAnswers())
                                                .build();
                                        default -> ShortAnswerQuestion.builder()
                                                .quiz(quiz)
                                                .description(quizRequestDTO.getDescription())
                                                .matchAnswer(questionRequestDTO.getMatchAnswers().stream().findFirst().orElse(null))
                                                .build();
                                    }
                            )
                    );
                }
                quiz.setQuestions(questions);
                Set<Quiz> quizzes = classroom.getQuizzes();
                quizzes.add(quiz);
                classroomRepository.save(classroom);

                quizzes.stream()
                        .map(Quiz::getQuestions).
                        flatMap(Set::stream)
                        .forEach(question -> question.setAnswers(null));

                return QuizResponseDTO.builder()
                        .quizzes(quizzes)
                        .build();
            } else {
                throw new AuthorizationException("Teacher not allow to add a quiz to the classroom");
            }
        } else {
            throw new ResourceNotFoundException("No classroom for given id");
        }
    }

    @Override
    public QuizResponseDTO deleteQuiz(String teacherUsername, Long quizId) {
        Optional<Quiz> optionalQuiz = quizRepository.findById(quizId);
        if (optionalQuiz.isPresent()) {
            Quiz quiz = optionalQuiz.get();
            Classroom classroom = quiz.getClassroom();
            if (teacherUsername.equals(classroom.getTeacher().getUsername())) {
                quizRepository.deleteById(quizId);
                Set<Quiz> quizzes = classroom.getQuizzes();

                quizzes.stream()
                        .map(Quiz::getQuestions).
                        flatMap(Set::stream)
                        .forEach(question -> question.setAnswers(null));

                return QuizResponseDTO.builder()
                        .quizzes(quizzes)
                        .build();
            } else {
                throw new AuthorizationException("Teacher not allow to delete the quiz from the classroom");
            }
        } else {
            throw new ResourceNotFoundException("No quiz for given quiz id");
        }
    }


    @Override
    public QuizResponseDTO viewQuizzes(String username, String type, Long classroomId) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classroomId);
        if (optionalClassroom.isPresent()) {
            Classroom classroom = optionalClassroom.get();
            if (TEACHER.equals(type)) {
                if (username.equals(classroom.getTeacher().getUsername())) {
                    Set<Quiz> quizzes = classroom.getQuizzes();
                    quizzes.stream()
                            .map(Quiz::getQuestions).
                            flatMap(Set::stream)
                            .forEach(question -> question.setAnswers(null));

                    return QuizResponseDTO.builder()
                            .quizzes(quizzes)
                            .build();
                } else {
                    throw new AuthorizationException("Teacher not allow to delete the quiz from the classroom");
                }
            } else {
                Set<Quiz> quizzes = classroom.getQuizzes();
                quizzes.stream()
                        .map(Quiz::getQuestions)
                        .flatMap(Set::stream)
                        .map(Question::getAnswers)
                        .flatMap(Set::stream)
                        .collect(Collectors.toSet())
                        .removeIf(answer -> !username.equals(answer.getStudent().getUsername()));

                return QuizResponseDTO.builder()
                        .quizzes(quizzes)
                        .build();
            }

        } else {
            throw new ResourceNotFoundException("No classroom for given classroom id");
        }
    }

}
