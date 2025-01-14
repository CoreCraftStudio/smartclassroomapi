package com.smart.classroom.smartclassroom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {

    String username;
    String profileName;
    String email;
    String phone;
    String parentUsername;
    String parentPhone;
    String parentEmail;
}
