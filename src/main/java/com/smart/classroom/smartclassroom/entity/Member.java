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
    String username;

    String phone;

    String email;

    String profileName;

    @JsonIgnore
    String password;

    Boolean freemium;

}
