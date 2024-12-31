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
public class Teacher extends Member {

    @OneToMany(mappedBy = "teacher")
    @JsonIgnore
    Set<Classroom> classrooms;

    @Builder
    public Teacher(String username, String phone, String email, String profileName, String password, Boolean freemium, Set<Classroom> classrooms) {
        super(username, phone, email, profileName, password, freemium);
        this.classrooms = classrooms;
    }

}
