package com.smart.classroom.smartclassroom.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constant {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class UserConstant {
        public static final String TEACHER = "teacher";
        public static final String STUDENT = "student";
        public static final String PARENT = "parent";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class QuestionAndAnswerTypeConstant {
        public static final String MULTIPLE_RESPONSE = "multiple-response";
        public static final String MULTIPLE_CHOICE = "multiple-choice";
        public static final String SHORT_ANSWER = "short-answer";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class JWTConstant {
        public static final String ISSUER = "core-craft-solution";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ExceptionMessage {
        public static final String NO_CLASSROOM_FOR_ID = "No classroom for given id";
        public static final String NO_STUDENT_FOR_USERNAME = "No student for given username";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class PATH {
        public static final String QUIZZES = "/quizzes";
    }


}
