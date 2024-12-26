package com.smart.classroom.smartclassroom.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("short-answer")
@Getter
@Setter
@NoArgsConstructor
public class ShortAnswerAnswer extends Answer {
    String answer;

    @Builder
    public ShortAnswerAnswer(Long id, Student student, Double mark, String answer, Question question) {
        super(id, student, mark, question);
        this.answer = answer;
    }
}
