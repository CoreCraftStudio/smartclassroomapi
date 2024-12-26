package com.smart.classroom.smartclassroom.contoller;

import com.smart.classroom.smartclassroom.dto.UserRequestDTO;
import com.smart.classroom.smartclassroom.dto.UserResponseDTO;
import com.smart.classroom.smartclassroom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public UserResponseDTO setUser(@RequestBody UserRequestDTO userRequestDTO) {
        return userService.createUser(userRequestDTO);
    }

    @PostMapping("/login")
    public UserResponseDTO getUser(@RequestBody UserRequestDTO userRequestDTO) {
        return userService.viewUser(userRequestDTO);
    }


}
