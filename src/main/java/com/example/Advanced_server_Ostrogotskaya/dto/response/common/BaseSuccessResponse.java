package com.example.Advanced_server_Ostrogotskaya.dto.response.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseSuccessResponse {
    public Set<Integer> codes = new HashSet<>();

    Integer statusCode = 0;

    Boolean success = true;

    public BaseSuccessResponse(Integer statusCode) {

        this.statusCode = statusCode;
    }

    public BaseSuccessResponse(Integer statusCode, Boolean success) {
        this.statusCode = statusCode;
        this.success = success;
    }

    public BaseSuccessResponse(int code, String message) {
    }
}
