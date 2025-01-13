package com.smart.classroom.smartclassroom.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuizMark {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @OneToOne
    @JoinColumn(name = "student_username", referencedColumnName = "username")
    Student student;

    @OneToOne
    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    Quiz quiz;

    Double totalMark;
}
