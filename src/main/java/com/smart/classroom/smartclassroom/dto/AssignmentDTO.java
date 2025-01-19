package com.smart.classroom.smartclassroom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentDTO {
    Long id;
    String name;
    String description;
    Long attachmentId;
    Long matchAttachmentId;
    Long submissionId;
    String answer;
    Long submissionAttachmentId;
    Double mark;
    Double maxMark;
}
