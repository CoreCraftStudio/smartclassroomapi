package com.smart.classroom.smartclassroom.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    Student student;

    String description;

    Long attachmentId;

    Double mark;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    Assignment assignment;
}
