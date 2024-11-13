package com.example.Advanced_server_Ostrogotskaya.authServiceTests;

import com.example.Advanced_server_Ostrogotskaya.ConstantTest;
import com.example.Advanced_server_Ostrogotskaya.dto.request.LoginUserRequest;
import com.example.Advanced_server_Ostrogotskaya.dto.request.RegisterUserRequest;
import com.example.Advanced_server_Ostrogotskaya.dto.response.LoginUserResponse;
import com.example.Advanced_server_Ostrogotskaya.entities.UserEntity;
import com.example.Advanced_server_Ostrogotskaya.errors.CustomException;
import com.example.Advanced_server_Ostrogotskaya.mapper.UserMapper;
import com.example.Advanced_server_Ostrogotskaya.repositories.AuthRepository;
import com.example.Advanced_server_Ostrogotskaya.security.JwtService;
import com.example.Advanced_server_Ostrogotskaya.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class RegistrationTests extends ConstantTest {

    @InjectMocks
    AuthServiceImpl authService;

    @Mock
    JwtService jwtService;

    @Mock
    UserMapper userMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AuthRepository authRepository;

    @Test
    @DisplayName("Correct create new user")
    void correctCreateNewUser() {
        UserEntity user = createUser();
        RegisterUserRequest registerUserRequest = createRegisterUserDto();
        LoginUserResponse loginUserResponse = createLoginUserDto();
        when(authRepository.existsByEmail(registerUserRequest.getEmail())).thenReturn(false);
        when(userMapper.toUser(registerUserRequest)).thenReturn(user);
        when(passwordEncoder.encode(registerUserRequest.getPassword())).thenReturn(registerUserRequest.getPassword());
        when(authRepository.save(user)).thenReturn(user);
        when(authRepository.findByEmail(registerUserRequest.getEmail())).thenReturn(Optional.of(user));
        when(userMapper.toLogin(user)).thenReturn(loginUserResponse);
        when(jwtService.generateToken(user.getId())).thenReturn(TOKEN);
        LoginUserResponse response = authService.registerUser(registerUserRequest);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(TOKEN, response.getToken());
        Assertions.assertEquals(loginUserResponse, response);
    }

    @Test
    @DisplayName("User already exists")
    void userAlreadyExists() {
        RegisterUserRequest registerUserRequest = createRegisterUserDto();
        when(authRepository.existsByEmail(registerUserRequest.getEmail())).thenReturn(true);
        Assertions.assertThrows(CustomException.class, () ->
                authService.registerUser(registerUserRequest));
    }

    @Test
    @DisplayName("Could not find user")
    void userNotFound() {
        RegisterUserRequest registerUserRequest = createRegisterUserDto();
        when(authRepository.existsByEmail(registerUserRequest.getEmail())).thenReturn(false);
        when(userMapper.toUser(registerUserRequest)).thenReturn(new UserEntity());
        when(passwordEncoder.encode(registerUserRequest.getPassword())).thenReturn(registerUserRequest.getPassword());
        when(authRepository.save(any(UserEntity.class))).thenReturn(new UserEntity());
        when(authRepository.findByEmail(registerUserRequest.getEmail())).thenReturn(Optional.empty());
        Assertions.assertThrows(CustomException.class, () -> {
            authService.registerUser(registerUserRequest);
        });
    }

    @Test
    @DisplayName("Correct login user")
    void correctLoginUser() {
        UserEntity user = createUser();
        LoginUserRequest loginUserRequest = loginUserRequestDto();
        LoginUserResponse loginUserResponse = createLoginUserDto();
        when(authRepository.existsByEmail(loginUserRequest.getEmail())).thenReturn(true);
        when(authRepository.findByEmail(loginUserRequest.getEmail())).thenReturn(Optional.of(user));
        when(userMapper.toLogin(user)).thenReturn(loginUserResponse);
        when(jwtService.generateToken(user.getId())).thenReturn(TOKEN);
        LoginUserResponse response = authService.loginUser(loginUserRequest);
        LoginUserResponse actualResponceAuthService = response;
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(actualResponceAuthService);
        Assertions.assertEquals(TOKEN, actualResponceAuthService.getToken());
        Assertions.assertEquals(loginUserResponse, actualResponceAuthService);
    }

    @Test
    @DisplayName("Could not find user Login")
    void userNotFoundLogin() {
        LoginUserRequest loginUserRequest = loginUserRequestDto();
        when(authRepository.existsByEmail(loginUserRequest.getEmail())).thenReturn(false);
        Assertions.assertThrows(CustomException.class, () -> {
            authService.loginUser(loginUserRequest);
        });
    }

    private LoginUserRequest loginUserRequestDto() {
        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail(EMAIL);
        loginUserRequest.setPassword(passwordEncoder.encode(EMAIL));
        return loginUserRequest;
    }

    private LoginUserResponse createLoginUserDto() {
        LoginUserResponse loginUserResponse = new LoginUserResponse();
        loginUserResponse.setMessage(MESSAGE);
        loginUserResponse.setAvatar(AVATAR);
        loginUserResponse.setEmail(EMAIL);
        loginUserResponse.setId(UUID.randomUUID());
        loginUserResponse.setToken(TOKEN);
        loginUserResponse.setRole(ROLE);
        loginUserResponse.setName(NAME);
        return loginUserResponse;
    }

    private RegisterUserRequest createRegisterUserDto() {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setEmail(EMAIL);
        registerUserRequest.setPassword(passwordEncoder.encode(PASSWORD));
        registerUserRequest.setAvatar(AVATAR);
        registerUserRequest.setName(NAME);
        registerUserRequest.setRole(ROLE);
        return registerUserRequest;
    }

    private UserEntity createUser() {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setAvatar(AVATAR);
        user.setName(NAME);
        user.setEmail(EMAIL);
        user.setPassword(passwordEncoder.encode(PASSWORD));
        user.setRole(ROLE);
        return user;
    }

}
