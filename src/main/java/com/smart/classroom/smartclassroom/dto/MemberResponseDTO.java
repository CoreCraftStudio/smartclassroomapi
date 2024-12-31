package com.smart.classroom.smartclassroom.dto;

import com.smart.classroom.smartclassroom.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDTO {

    Member member;
    String type;
    String token;
}
