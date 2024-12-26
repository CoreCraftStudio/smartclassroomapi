package com.smart.classroom.smartclassroom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String name;

    String description;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.REMOVE)
    Set<Question> questions;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    @JsonIgnore
    Classroom classroom;
}
