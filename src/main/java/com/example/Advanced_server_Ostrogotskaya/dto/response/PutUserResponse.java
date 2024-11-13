package com.example.Advanced_server_Ostrogotskaya.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class PutUserResponse {

    private String avatar;

    private String name;

    private String email;

    private UUID id;

    private String role;

}
