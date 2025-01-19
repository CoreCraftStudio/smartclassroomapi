package com.smart.classroom.smartclassroom.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Entity
@Builder
@Getter
@Setter
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String name;

    String description;

    Long attachmentId;

    Long matchAttachmentId;

    Double maxMark;

    @OneToMany(mappedBy = "assignment")
    Set<Submission> submissions;

    @ManyToOne
    @JoinColumn(name = "classroom_id", nullable = false)
    Classroom classroom;
}
