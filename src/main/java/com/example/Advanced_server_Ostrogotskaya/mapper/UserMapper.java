package com.example.Advanced_server_Ostrogotskaya.mapper;

import com.example.Advanced_server_Ostrogotskaya.dto.request.PutUserRequest;
import com.example.Advanced_server_Ostrogotskaya.dto.request.RegisterUserRequest;
import com.example.Advanced_server_Ostrogotskaya.dto.response.LoginUserResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.PublicUserResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.PutUserResponse;
import com.example.Advanced_server_Ostrogotskaya.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    UserEntity toUser(RegisterUserRequest registerUserRequest);

    @Mapping(target = "message", ignore = true)
    @Mapping(target = "token", ignore = true)
    LoginUserResponse toLogin(UserEntity userEntity);

    List<PublicUserResponse> toPublicUserResponseList(List<UserEntity> userEntities);

    PutUserResponse toPutUser(UserEntity userEntity);

    PublicUserResponse toPublicUser(UserEntity userEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateUserFromRequest(PutUserRequest putUserRequest, @MappingTarget UserEntity userEntity);
}
