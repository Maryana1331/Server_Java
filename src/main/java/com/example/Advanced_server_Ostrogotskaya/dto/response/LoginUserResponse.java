package com.example.Advanced_server_Ostrogotskaya.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class LoginUserResponse {

    private String message;

    private String avatar;

    private String email;

    private UUID id;

    private String name;

    private String role;

    private String token;
}
