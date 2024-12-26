package com.smart.classroom.smartclassroom.service.impl;

import com.smart.classroom.smartclassroom.dto.UserRequestDTO;
import com.smart.classroom.smartclassroom.dto.UserResponseDTO;
import com.smart.classroom.smartclassroom.entity.Parent;
import com.smart.classroom.smartclassroom.entity.Student;
import com.smart.classroom.smartclassroom.entity.Teacher;
import com.smart.classroom.smartclassroom.entity.User;
import com.smart.classroom.smartclassroom.exception.AuthenticationException;
import com.smart.classroom.smartclassroom.exception.ValidationException;
import com.smart.classroom.smartclassroom.repository.UserRepository;
import com.smart.classroom.smartclassroom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.smart.classroom.smartclassroom.util.Constant.UserConstant.STUDENT;
import static com.smart.classroom.smartclassroom.util.Constant.UserConstant.TEACHER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(userRequestDTO.getEmail());
        if (optionalUser.isEmpty()) {
            UserResponseDTO.UserResponseDTOBuilder userResponseDTOBuilder = UserResponseDTO.builder();
            String encodedPassword = passwordEncoder.encode(userRequestDTO.getPassword());
            userRepository.save(switch (userRequestDTO.getType()) {
                        case TEACHER -> {
                            Teacher teacher = Teacher.builder()
                                    .email(userRequestDTO.getEmail())
                                    .name(userRequestDTO.getName())
                                    .password(encodedPassword)
                                    .build();
                            userResponseDTOBuilder.user(teacher);
                            yield teacher;
                        }
                        case STUDENT -> {
                            Student student = Student.builder()
                                    .email(userRequestDTO.getEmail())
                                    .name(userRequestDTO.getName())
                                    .password(encodedPassword)
                                    .build();
                            userResponseDTOBuilder.user(student);
                            yield student;
                        }
                        default -> {
                            Parent parent = Parent.builder()
                                    .email(userRequestDTO.getEmail())
                                    .name(userRequestDTO.getName())
                                    .password(encodedPassword)
                                    .build();
                            userResponseDTOBuilder.user(parent);
                            yield parent;
                        }
                    }
            );
            return userResponseDTOBuilder
                    .type(userRequestDTO.getType())
                    .build();

        } else {
            throw new ValidationException("User already exists");
        }
    }

    @Override
    public UserResponseDTO viewUser(UserRequestDTO userRequestDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(userRequestDTO.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(userRequestDTO.getPassword(), user.getPassword())) {
                String type = userRepository.findTypeByEmail(userRequestDTO.getEmail());
                return UserResponseDTO.builder()
                        .user(user)
                        .type(type)
                        .build();
            } else {
                throw new AuthenticationException("User name or password is incorrect");
            }
        } else {
            throw new ValidationException("No user for given email");
        }

    }

}
