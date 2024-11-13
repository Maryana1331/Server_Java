package com.example.Advanced_server_Ostrogotskaya.service;

import com.example.Advanced_server_Ostrogotskaya.dto.request.LoginUserRequest;
import com.example.Advanced_server_Ostrogotskaya.dto.request.RegisterUserRequest;
import com.example.Advanced_server_Ostrogotskaya.dto.response.LoginUserResponse;

public interface AuthService {
    LoginUserResponse  registerUser(RegisterUserRequest registerUserRequest);

    LoginUserResponse loginUser(LoginUserRequest loginUserRequest);
}
