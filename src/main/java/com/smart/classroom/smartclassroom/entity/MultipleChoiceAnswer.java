package com.smart.classroom.smartclassroom.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@DiscriminatorValue("multiple-choice")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MultipleChoiceAnswer extends Answer {
    String choice;

    @Builder
    public MultipleChoiceAnswer(Long id, Student student, Double mark, String choice, Question question) {
        super(id, student, mark, question);
        this.choice = choice;
    }
}
