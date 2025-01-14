package com.smart.classroom.smartclassroom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String name;

    @ManyToOne
    @JoinColumn(name = "teacher_email", nullable = false)
    @JsonIgnore
    Teacher teacher;

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.REMOVE)
    @JsonIgnore
    Set<Assignment> assignments;

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.REMOVE)
    @JsonIgnore
    Set<Quiz> quizzes;

    @ManyToMany
    @JoinTable(
            name = "enrollment",
            joinColumns = @JoinColumn(name = "classroom_id"),
            inverseJoinColumns = @JoinColumn(name = "student_email"))
    @JsonIgnore
    Set<Student> students;

}
