package com.example.Advanced_server_Ostrogotskaya.service.impl;

import com.example.Advanced_server_Ostrogotskaya.constants.ErrorCodes;
import com.example.Advanced_server_Ostrogotskaya.dto.request.PutUserRequest;
import com.example.Advanced_server_Ostrogotskaya.dto.response.PublicUserResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.PutUserResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.common.BaseSuccessResponse;
import com.example.Advanced_server_Ostrogotskaya.entities.UserEntity;
import com.example.Advanced_server_Ostrogotskaya.errors.CustomException;
import com.example.Advanced_server_Ostrogotskaya.mapper.UserMapper;
import com.example.Advanced_server_Ostrogotskaya.repositories.AuthRepository;
import com.example.Advanced_server_Ostrogotskaya.repositories.UserRepository;
import com.example.Advanced_server_Ostrogotskaya.security.CustomUserDetails;
import com.example.Advanced_server_Ostrogotskaya.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final AuthRepository authRepository;

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    @Override
    public List<PublicUserResponse> getAllUsers() {
        List<UserEntity> userEntities = authRepository.findAll();
        return userMapper.toPublicUserResponseList(userEntities);
    }

    @Override
    public PublicUserResponse getUserInfoById(UUID id) {
        UserEntity user = authRepository.findById(id).orElseThrow();
        return userMapper.toPublicUser(user);
    }

    @Override
    public PublicUserResponse getUserInfo() {
        UUID id = UUID.fromString(((CustomUserDetails) (SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal())).getUsername());

        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));

        return userMapper.toPublicUser(userEntity);
    }

    @Override
    public PutUserResponse replaceUser(PutUserRequest putUserRequest) {
        UUID id = UUID.fromString(((CustomUserDetails) (SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal())).getUsername());
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));
        userMapper.updateUserFromRequest(putUserRequest, user);

        PutUserResponse putUserResponse = userMapper.toPutUser(user);

        userRepository.save(user);
        return putUserResponse;
    }

    @Override
    @Transactional
    public BaseSuccessResponse deleteUser() {
        UUID id = UUID.fromString(((CustomUserDetails) (SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal())).getUsername());
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));
        userRepository.delete(user);
        return new BaseSuccessResponse();
    }
}
