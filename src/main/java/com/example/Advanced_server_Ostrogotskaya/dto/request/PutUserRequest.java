package com.example.Advanced_server_Ostrogotskaya.dto.request;

import com.example.Advanced_server_Ostrogotskaya.constants.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class PutUserRequest {

    @NotNull(message = ValidationConstants.USER_AVATAR_NOT_NULL)
    private String avatar;

    @Email
    @NotNull(message = ValidationConstants.USER_EMAIL_NOT_NULL)
    @Size(min = 3, max = 25)
    private String email;

    @NotBlank(message = ValidationConstants.USER_NAME_HAS_TO_BE_PRESENT)
    @Size(min = 3, max = 25)
    private String name;

    @NotBlank(message = ValidationConstants.USER_ROLE_NOT_NULL)
    private String role;
}
