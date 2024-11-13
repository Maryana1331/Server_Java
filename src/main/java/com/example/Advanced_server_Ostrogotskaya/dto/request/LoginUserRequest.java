package com.example.Advanced_server_Ostrogotskaya.dto.request;

import com.example.Advanced_server_Ostrogotskaya.constants.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Setter
@Getter
public class LoginUserRequest {

    @Email(message = ValidationConstants.USER_EMAIL_NOT_VALID)
    @NotBlank(message = ValidationConstants.USER_EMAIL_NOT_NULL)
    @Size(min = 3, max = 100, message = ValidationConstants.EMAIL_SIZE_NOT_VALID)
    private String email;

    @NotBlank(message = ValidationConstants.PASSWORD_NOT_VALID)
    private String password;
}
