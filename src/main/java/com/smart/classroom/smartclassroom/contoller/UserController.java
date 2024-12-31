package com.smart.classroom.smartclassroom.contoller;

import com.smart.classroom.smartclassroom.dto.MemberRequestDTO;
import com.smart.classroom.smartclassroom.dto.MemberResponseDTO;
import com.smart.classroom.smartclassroom.service.AuthService;
import com.smart.classroom.smartclassroom.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final MemberService memberService;
    private final AuthService authService;

    @PostMapping("/user")
    public MemberResponseDTO setUser(@RequestBody MemberRequestDTO memberRequestDTO) {
        return memberService.createMember(memberRequestDTO);
    }

    @PostMapping("/auth")
    public MemberResponseDTO getUser(@RequestBody MemberRequestDTO memberRequestDTO) {
        MemberResponseDTO memberResponseDTO = memberService.viewMember(memberRequestDTO);
        memberResponseDTO.setToken(authService.generateToken(memberRequestDTO.getUsername()));
        return memberResponseDTO;
    }

}

