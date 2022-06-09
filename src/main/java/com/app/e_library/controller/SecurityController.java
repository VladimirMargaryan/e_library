package com.app.e_library.controller;

import com.app.e_library.persistence.entity.UserEntity;
import com.app.e_library.security.JWTUtil;
import com.app.e_library.service.UserService;
import com.app.e_library.service.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/security")
@AllArgsConstructor
public class SecurityController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request,
                           HttpServletResponse response) throws IOException {

        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (jwtUtil.isTokenValid(authorizationHeader)) {

            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());

                String username = jwtUtil.getUsername(refreshToken);
                UserEntity user = UserDto.mapToEntity(userService.getByEmail(username));

                String accessToken = jwtUtil.getAccessToken(user, request);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            } catch (Exception e) {
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());

                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }

        } else {
            throw new RuntimeException("Refresh token is missing!");
        }

    }
}
