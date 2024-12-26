package com.smart.classroom.smartclassroom.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constant {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class UserConstant {
        public static final String TEACHER = "teacher";
        public static final String STUDENT = "student";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class QuestionAndAnswerTypeConstant {
        public static final String MULTIPLE_RESPONSE = "multiple-response";
        public static final String MULTIPLE_CHOICE = "multiple-choice";
    }


}
