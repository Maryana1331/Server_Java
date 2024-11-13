package com.example.Advanced_server_Ostrogotskaya.service;

import com.example.Advanced_server_Ostrogotskaya.dto.request.PutUserRequest;
import com.example.Advanced_server_Ostrogotskaya.dto.response.PublicUserResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.PutUserResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.common.BaseSuccessResponse;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public interface UserService {

    List<PublicUserResponse> getAllUsers();

    PublicUserResponse getUserInfoById(UUID id);

    PublicUserResponse getUserInfo();

    PutUserResponse replaceUser(PutUserRequest putUserRequest);

    BaseSuccessResponse deleteUser();
}
