package com.example.Advanced_server_Ostrogotskaya.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class PublicUserResponse {
    private String avatar;

    private String name;

    private String email;

    private UUID id;

    private String role;

}
