package com.example.Advanced_server_Ostrogotskaya.errors;

import com.example.Advanced_server_Ostrogotskaya.constants.ErrorCodes;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCodes errorCode;

    public CustomException(ErrorCodes errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCodes getErrorCode() {
        return errorCode;
    }
}
