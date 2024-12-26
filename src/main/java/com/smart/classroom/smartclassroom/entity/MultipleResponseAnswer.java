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
public class MultipleResponseAnswer extends Answer {
    Set<String> responses;

    @Builder
    public MultipleResponseAnswer(Long id, Student student, Double mark, Set<String> responses, Question question) {
        super(id, student, mark, question);
        this.responses = responses;
    }
}
