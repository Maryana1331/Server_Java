package com.example.Advanced_server_Ostrogotskaya.controller;

import com.example.Advanced_server_Ostrogotskaya.constants.ValidationConstants;
import com.example.Advanced_server_Ostrogotskaya.dto.request.PutUserRequest;
import com.example.Advanced_server_Ostrogotskaya.dto.response.PublicUserResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.PutUserResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.common.BaseSuccessResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.common.CustomSuccessResponse;
import com.example.Advanced_server_Ostrogotskaya.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping("/all")
    public ResponseEntity<CustomSuccessResponse<List<PublicUserResponse>>> getUserAll() {
        return ResponseEntity.ok(new CustomSuccessResponse<>(userService.getAllUsers()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomSuccessResponse<PublicUserResponse>> getUserInfoById(
            @PathVariable @UUID(message = ValidationConstants.MAX_UPLOAD_SIZE_EXCEEDED) String id) {
        return ResponseEntity.ok(new CustomSuccessResponse<>(userService.getUserInfoById(java.util.UUID.fromString(id))));
    }

    @GetMapping("/info")
    public ResponseEntity<CustomSuccessResponse<PublicUserResponse>> getUserInfo() {
        return ResponseEntity.ok(new CustomSuccessResponse<>(userService.getUserInfo()));
    }

    @PutMapping
    public ResponseEntity<CustomSuccessResponse<PutUserResponse>> userReplace(
            @Valid @RequestBody PutUserRequest putUserRequest) {
        return ResponseEntity.ok(new CustomSuccessResponse<>(userService.replaceUser(putUserRequest)));
    }

    @DeleteMapping
    public ResponseEntity<BaseSuccessResponse> userDelete() {
        return ResponseEntity.ok(userService.deleteUser());
    }
}
