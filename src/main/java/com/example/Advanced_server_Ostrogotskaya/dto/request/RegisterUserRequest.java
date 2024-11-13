package com.example.Advanced_server_Ostrogotskaya.dto.request;

import com.example.Advanced_server_Ostrogotskaya.constants.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class RegisterUserRequest {
    @NotBlank(message = ValidationConstants.USER_AVATAR_NOT_NULL)
    private String avatar;

    @NotBlank(message = ValidationConstants.USER_NAME_HAS_TO_BE_PRESENT)
    @Length(min = 3, max = 25, message = ValidationConstants.USERNAME_SIZE_NOT_VALID)
    private String name;

    @Email(message = ValidationConstants.USER_EMAIL_NOT_VALID)
    @Length(min = 3, max = 100, message = ValidationConstants.EMAIL_SIZE_NOT_VALID)
    @NotBlank(message = ValidationConstants.USER_EMAIL_NOT_VALID)
    private String email;

    @NotBlank(message = ValidationConstants.USER_PASSWORD_NOT_VALID)
    private String password;

    @NotBlank(message = ValidationConstants.USER_ROLE_NOT_NULL)
    @Length(min = 3, max = 25, message = ValidationConstants.ROLE_SIZE_NOT_VALID)
    private String role;
}
