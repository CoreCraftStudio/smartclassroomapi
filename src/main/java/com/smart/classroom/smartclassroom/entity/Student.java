package com.smart.classroom.smartclassroom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@DiscriminatorValue("student")
@Getter
@Setter
@NoArgsConstructor
public class Student extends Member {

    @ManyToMany(mappedBy = "students")
    @JsonIgnore
    Set<Classroom> classrooms;

    @ManyToOne
    @JoinColumn(name = "parent_email")
    Parent parent;

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    Set<Answer> answers;

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    Set<Submission> submissions;

    @Builder
    public Student(String username, String phone, String email, String profileName, String password, Boolean freemium, Set<Classroom> classrooms, Parent parent, Set<Answer> answers, Set<Submission> submissions) {
        super(username, phone, email, profileName, password, freemium);
        this.classrooms = classrooms;
        this.parent = parent;
        this.answers = answers;
        this.submissions = submissions;
    }

}
