package com.smart.classroom.smartclassroom.service.impl;

import com.smart.classroom.smartclassroom.dto.MemberRequestDTO;
import com.smart.classroom.smartclassroom.dto.MemberResponseDTO;
import com.smart.classroom.smartclassroom.entity.Member;
import com.smart.classroom.smartclassroom.entity.Parent;
import com.smart.classroom.smartclassroom.entity.Student;
import com.smart.classroom.smartclassroom.entity.Teacher;
import com.smart.classroom.smartclassroom.exception.AuthenticationException;
import com.smart.classroom.smartclassroom.exception.ResourceNotFoundException;
import com.smart.classroom.smartclassroom.exception.ValidationException;
import com.smart.classroom.smartclassroom.repository.UserRepository;
import com.smart.classroom.smartclassroom.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.smart.classroom.smartclassroom.util.Constant.UserConstant.STUDENT;
import static com.smart.classroom.smartclassroom.util.Constant.UserConstant.TEACHER;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberResponseDTO createMember(MemberRequestDTO memberRequestDTO) {
        Optional<Member> optionalUser = userRepository.findByEmail(memberRequestDTO.getEmail());
        if (optionalUser.isEmpty()) {
            MemberResponseDTO.MemberResponseDTOBuilder memberResponseDTOBuilder = MemberResponseDTO.builder();
            String encodedPassword = passwordEncoder.encode(memberRequestDTO.getPassword());
            userRepository.save(switch (memberRequestDTO.getType()) {
                        case TEACHER -> {
                            Teacher teacher = Teacher.builder()
                                    .email(memberRequestDTO.getEmail())
                                    .name(memberRequestDTO.getName())
                                    .password(encodedPassword)
                                    .build();
                            memberResponseDTOBuilder.member(teacher);
                            yield teacher;
                        }
                        case STUDENT -> {
                            Student student = Student.builder()
                                    .email(memberRequestDTO.getEmail())
                                    .name(memberRequestDTO.getName())
                                    .password(encodedPassword)
                                    .build();
                            memberResponseDTOBuilder.member(student);
                            yield student;
                        }
                        default -> {
                            Parent parent = Parent.builder()
                                    .email(memberRequestDTO.getEmail())
                                    .name(memberRequestDTO.getName())
                                    .password(encodedPassword)
                                    .build();
                            memberResponseDTOBuilder.member(parent);
                            yield parent;
                        }
                    }
            );
            return memberResponseDTOBuilder
                    .type(memberRequestDTO.getType())
                    .build();

        } else {
            throw new ValidationException("User already exists");
        }
    }

    @Override
    public MemberResponseDTO viewMember(MemberRequestDTO memberRequestDTO) {
        Optional<Member> optionalUser = userRepository.findByEmail(memberRequestDTO.getEmail());
        if (optionalUser.isPresent()) {
            Member member = optionalUser.get();
            if (passwordEncoder.matches(memberRequestDTO.getPassword(), member.getPassword())) {
                String type = userRepository.findTypeByEmail(memberRequestDTO.getEmail());
                return MemberResponseDTO.builder()
                        .member(member)
                        .type(type)
                        .build();
            } else {
                throw new AuthenticationException("User name or password is incorrect");
            }
        } else {
            throw new ResourceNotFoundException("No user for given email");
        }
    }

}
