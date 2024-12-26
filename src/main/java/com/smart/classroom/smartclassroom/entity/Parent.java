package com.smart.classroom.smartclassroom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Set;

@Entity
@DiscriminatorValue("parent")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Parent extends User {

    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    Set<Student> students;

    @Builder
    public Parent(String email, String name, String password, Set<Student> students) {
        super(email, name, password);
        this.students = students;
    }
}
