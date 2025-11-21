package com.vaga.ai.gs.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.vaga.ai.gs.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${spring.security.token.secret}")
    private String secret;

    private Instant genExpirationDate() {
        // Expira em 2 horas - fuso horário UTC-3 Brasil/SP)
        return LocalDateTime.now().plusHours(2)
                .toInstant(ZoneOffset.of("-03:00"));
    }

    public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String token = JWT.create()
                .withIssuer("auth-api")
                .withSubject(user.getEmail())
                .withExpiresAt(genExpirationDate())
                .sign(algorithm);
        return token;
    }

    public String validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm)
                .withIssuer("auth-api")
                .build()
                .verify(token)
                .getSubject();
    }
}
