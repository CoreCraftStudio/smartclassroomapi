package com.smart.classroom.smartclassroom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class Member {

    @Id
    String email;

    String name;

    @JsonIgnore
    String password;

}
