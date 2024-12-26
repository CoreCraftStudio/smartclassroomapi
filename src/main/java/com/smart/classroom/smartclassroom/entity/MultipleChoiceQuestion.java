package com.smart.classroom.smartclassroom.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

import java.util.Set;


@Entity
@DiscriminatorValue("multiple-choice")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MultipleChoiceQuestion extends Question {
    Set<String> choices;
    String matchChoice;

    @Builder
    public MultipleChoiceQuestion(Long id, String description, Quiz quiz, Set<Answer> answers, Set<String> choices, String matchChoice) {
        super(id, description, quiz, answers);
        this.choices = choices;
        this.matchChoice = matchChoice;
    }
}
