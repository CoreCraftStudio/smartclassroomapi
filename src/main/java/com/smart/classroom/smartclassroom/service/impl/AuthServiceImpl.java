package com.smart.classroom.smartclassroom.service.impl;

import com.smart.classroom.smartclassroom.exception.AuthenticationException;
import com.smart.classroom.smartclassroom.service.AuthService;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

import static com.smart.classroom.smartclassroom.util.Constant.JWTConstant.ISSUER;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${security.jwt.expiration.time}")
    private long expirationTime;

    private final UserDetailsService userDetailsService;

    private final SecretKey key = Jwts.SIG.HS256.key().build();

    @Override
    public String generateToken(String username) {
        Instant expiration = Instant.ofEpochSecond(Instant.now().getEpochSecond() + expirationTime);
        return Jwts.builder()
                .issuer(ISSUER)
                .subject(username)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(expiration))
                .signWith(key)
                .compact();
    }

    @Override
    public void verifyToken(String token) throws AuthenticationException {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parse(token);

        } catch (Exception e) {
            throw new AuthenticationException("Token verification failed");
        }

    }

    @Override
    public String getUsername(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }

}
