package com.app.e_library.security;


import com.app.e_library.persistence.entity.UserEntity;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JWTUtil {

    // 20 minute
    @Value("${jwt.access_token.active.time}")
    private Duration accessTokenActiveTime;

    // 1 day
    @Value("${jwt.refresh_token.active.time}")
    private Duration refreshTokenActiveTime;

    @Value("${jwt.secret}")
    private String secret;


    public String getAccessToken(User user, HttpServletRequest request) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenActiveTime.toMillis()))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("ROLE", user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .sign(getAlgorithm());
    }

    public String getAccessToken(UserEntity user, HttpServletRequest request) {
        return JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenActiveTime.toMillis()))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("ROLE", Collections.singletonList(user.getRole().getRollName()))
                .sign(getAlgorithm());
    }

    public String getRefreshToken(User user, HttpServletRequest request) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenActiveTime.toMillis()))
                .withIssuer(request.getRequestURL().toString())
                .sign(getAlgorithm());
    }


    public String getUsername(String token) {
        return verifyAndDecode(token).getSubject();
    }

    public String[] getRoles(String token) {
        return verifyAndDecode(token).getClaim("ROLE").asArray(String.class);
    }

    public Date getExpirationDate(String token) {
        return verifyAndDecode(token).getExpiresAt();
    }

    public Map<String, Claim> getClaims(String token) {
        return verifyAndDecode(token).getClaims();
    }

    public Boolean validateToken(String authorizationHeader) {
        return (authorizationHeader != null && authorizationHeader.startsWith("Bearer "));
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));
    }

    private DecodedJWT verifyAndDecode(String token) {
        return JWT.require(getAlgorithm()).build().verify(token);
    }

}
