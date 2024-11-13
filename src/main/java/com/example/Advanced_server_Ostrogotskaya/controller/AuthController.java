package com.example.Advanced_server_Ostrogotskaya.controller;

import com.example.Advanced_server_Ostrogotskaya.dto.request.LoginUserRequest;
import com.example.Advanced_server_Ostrogotskaya.dto.request.RegisterUserRequest;
import com.example.Advanced_server_Ostrogotskaya.dto.response.LoginUserResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.common.CustomSuccessResponse;
import com.example.Advanced_server_Ostrogotskaya.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthServiceImpl authService;

    @PostMapping("/register")
    public ResponseEntity<CustomSuccessResponse<LoginUserResponse>> registerUser(
            @Validated @RequestBody RegisterUserRequest registerUserRequest) {
        return  ResponseEntity.ok(new CustomSuccessResponse<>(authService.registerUser(registerUserRequest)));
    }

    @PostMapping("/login")
    public ResponseEntity<CustomSuccessResponse<LoginUserResponse>> loginUser(
            @Validated @RequestBody LoginUserRequest loginUserRequest) {
        return ResponseEntity.ok(new CustomSuccessResponse<>(authService.loginUser(loginUserRequest)));
    }
}
