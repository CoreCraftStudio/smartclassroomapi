package com.smart.classroom.smartclassroom.service.impl;

import com.smart.classroom.smartclassroom.entity.Member;
import com.smart.classroom.smartclassroom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = userRepository.findByEmail(username);
        String type = userRepository.findTypeByEmail(username);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            return User.builder()
                    .username(member.getEmail())
                    .password(member.getPassword())
                    .roles(type)
                    .build();
        } else {
            throw new UsernameNotFoundException("No user for given email");
        }
    }

}
