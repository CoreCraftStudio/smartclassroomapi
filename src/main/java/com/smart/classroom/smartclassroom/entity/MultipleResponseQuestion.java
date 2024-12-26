package com.smart.classroom.smartclassroom.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

import java.util.Set;

@Entity
@DiscriminatorValue("multiple-response")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MultipleResponseQuestion extends Question {
    Set<String> responses;
    Set<String> matchResponses;

    @Builder
    public MultipleResponseQuestion(Long id, String description, Quiz quiz, Set<Answer> answers, Set<String> matchResponses, Set<String> responses) {
        super(id, description, quiz, answers);
        this.matchResponses = matchResponses;
        this.responses = responses;
    }
}
