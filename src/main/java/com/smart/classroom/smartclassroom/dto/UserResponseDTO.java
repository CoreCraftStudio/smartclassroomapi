package com.smart.classroom.smartclassroom.dto;

import com.smart.classroom.smartclassroom.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    User user;
    String type;
}
