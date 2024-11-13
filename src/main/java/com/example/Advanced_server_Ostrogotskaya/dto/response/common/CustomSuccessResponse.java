package com.example.Advanced_server_Ostrogotskaya.dto.response.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomSuccessResponse<T> {
    private T data;

    private Integer statusCode;

    private Boolean success = true;

    public CustomSuccessResponse(Integer statusCode, Boolean success) {
        this.statusCode = statusCode;
        this.success = success;
    }

    public CustomSuccessResponse(T data) {
        this.data = data;
    }

}
