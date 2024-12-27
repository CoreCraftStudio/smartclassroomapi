package com.smart.classroom.smartclassroom.service;

import com.smart.classroom.smartclassroom.exception.AuthenticationException;

public interface AuthService {

    String generateToken(String username);

    void verifyToken(String token) throws AuthenticationException;

    String getUsername(String username);

}
