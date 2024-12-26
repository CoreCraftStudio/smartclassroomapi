package com.smart.classroom.smartclassroom.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Entity
@DiscriminatorValue("short-answer")
@Getter
@Setter
@NoArgsConstructor
public class ShortAnswerQuestion extends Question {
    String matchAnswer;

    @Builder
    public ShortAnswerQuestion(Long id, String description, Quiz quiz, Set<Answer> answers, String matchAnswer) {
        super(id, description, quiz, answers);
        this.matchAnswer = matchAnswer;
    }
}
