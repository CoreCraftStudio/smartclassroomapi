package com.smart.classroom.smartclassroom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Set;

@Entity
@DiscriminatorValue("teacher")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Teacher extends User {

    @OneToMany(mappedBy = "teacher")
    @JsonIgnore
    Set<Classroom> classrooms;

    @Builder
    public Teacher(String email, String name, String password, Set<Classroom> classrooms) {
        super(email, name, password);
        this.classrooms = classrooms;
    }

}
