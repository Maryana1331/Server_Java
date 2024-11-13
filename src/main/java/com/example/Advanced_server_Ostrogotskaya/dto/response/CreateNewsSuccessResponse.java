package com.example.Advanced_server_Ostrogotskaya.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class CreateNewsSuccessResponse {
    private Long id;

    private Integer statusCode = 0;

    private Boolean success = true;

    public CreateNewsSuccessResponse(Long id) {
        this.id = id;
    }
}

