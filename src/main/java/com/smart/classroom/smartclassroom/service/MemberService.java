package com.smart.classroom.smartclassroom.service;

import com.smart.classroom.smartclassroom.dto.MemberRequestDTO;
import com.smart.classroom.smartclassroom.dto.MemberResponseDTO;

public interface MemberService {

    MemberResponseDTO createMember(MemberRequestDTO memberRequestDTO);

    MemberResponseDTO viewMember(MemberRequestDTO memberRequestDTO);

}
