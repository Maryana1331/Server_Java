package com.example.Advanced_server_Ostrogotskaya.service.impl;

import com.example.Advanced_server_Ostrogotskaya.constants.ErrorCodes;
import com.example.Advanced_server_Ostrogotskaya.dto.request.LoginUserRequest;
import com.example.Advanced_server_Ostrogotskaya.dto.request.RegisterUserRequest;
import com.example.Advanced_server_Ostrogotskaya.dto.response.LoginUserResponse;
import com.example.Advanced_server_Ostrogotskaya.entities.UserEntity;
import com.example.Advanced_server_Ostrogotskaya.errors.CustomException;
import com.example.Advanced_server_Ostrogotskaya.mapper.UserMapper;
import com.example.Advanced_server_Ostrogotskaya.repositories.AuthRepository;
import com.example.Advanced_server_Ostrogotskaya.security.JwtService;
import com.example.Advanced_server_Ostrogotskaya.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final AuthRepository authRepository;

    @Value("${file.avatar}")
        private String uploadDir;

    @Override
    public LoginUserResponse registerUser(RegisterUserRequest registerUserRequest) {
        if (authRepository.existsByEmail(registerUserRequest.getEmail())) {
            throw new CustomException(ErrorCodes.USER_ALREADY_EXISTS);
        }

        UserEntity userEntity = userMapper.toUser(registerUserRequest);
        userEntity.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));

        authRepository.save(userEntity);

        UserEntity existingUser = authRepository.findByEmail(registerUserRequest.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));

        LoginUserResponse loginUserResponse = userMapper.toLogin(existingUser);
        loginUserResponse.setToken(jwtService.generateToken(userEntity.getId()));
        return loginUserResponse;
    }

        @Override
        public LoginUserResponse loginUser(LoginUserRequest loginUserRequest) {
            UserEntity user = authRepository.findByEmail(loginUserRequest.getEmail())
                    .orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));

            LoginUserResponse loginUserResponse = userMapper.toLogin(user);
            loginUserResponse.setToken(jwtService.generateToken(user.getId()));
            return loginUserResponse;
        }

}
