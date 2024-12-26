package com.smart.classroom.smartclassroom.service;

import com.smart.classroom.smartclassroom.dto.UserRequestDTO;
import com.smart.classroom.smartclassroom.dto.UserResponseDTO;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO userRequestDTO);

    UserResponseDTO viewUser(UserRequestDTO userRequestDTO);

}
