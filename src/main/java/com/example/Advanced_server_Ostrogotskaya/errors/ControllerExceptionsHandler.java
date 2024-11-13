package com.example.Advanced_server_Ostrogotskaya.errors;

import com.example.Advanced_server_Ostrogotskaya.constants.ErrorCodes;
import com.example.Advanced_server_Ostrogotskaya.dto.response.common.BaseSuccessResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.common.CustomSuccessResponse;
import io.jsonwebtoken.io.DecodingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.List;

@ControllerAdvice
public class ControllerExceptionsHandler {
    private final HttpHeaders responseHeaders = new HttpHeaders();

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BaseSuccessResponse> handleCustomException(CustomException e) {
        String message = e.getErrorCode().getMessage();
        responseHeaders.set("Exception", message);
        return ResponseEntity.badRequest()
                .headers(responseHeaders)
                .body(new BaseSuccessResponse(e.getErrorCode().getCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> MethodArgumentNotValidException(MethodArgumentNotValidException e) {

        var messages = e.getBindingResult().getFieldErrors().stream()
                .map(message -> ErrorCodes.typeErrors.get(message.getDefaultMessage())).toList();
        responseHeaders.set("Exception", messages.toString());
        if (messages.size() == 1) {
            return ResponseEntity.badRequest()
                    .headers(responseHeaders)
                    .body(new BaseSuccessResponse(messages.get(0)));
        } else {
            BaseSuccessResponse baseSuccessResponse = new BaseSuccessResponse();
            baseSuccessResponse.codes.addAll(messages);
            return ResponseEntity.badRequest()
                    .headers(responseHeaders)
                    .body(baseSuccessResponse);
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> HttpMessageNotReadableException(HttpMessageNotReadableException e) {

        String messages = e.getMessage();
        responseHeaders.set("Exception", messages);
        return ResponseEntity.badRequest()
                .headers(responseHeaders)
                .body(new BaseSuccessResponse(ErrorCodes.typeErrors
                        .get(e.getMessage())));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> NullPointerException(NullPointerException e) {
        String messages = e.getMessage();
        responseHeaders.set("Exception", messages);
        return ResponseEntity.badRequest().headers(responseHeaders).body(new BaseSuccessResponse(ErrorCodes.typeErrors
                .get(e.getMessage())));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> ConstraintViolationException(ConstraintViolationException e) {
        BaseSuccessResponse baseSuccessResponse = new BaseSuccessResponse();

        List<Integer> errorCodes = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .map(ErrorCodes.typeErrors::get)
                .toList();

        baseSuccessResponse.codes.addAll(errorCodes);
        baseSuccessResponse.setStatusCode(errorCodes.get(0));
        String messages = e.getMessage();
        responseHeaders.set("Exception", messages);
        return ResponseEntity.badRequest().headers(responseHeaders).body(baseSuccessResponse);
    }

    @ExceptionHandler(DecodingException.class)
    public ResponseEntity<BaseSuccessResponse> DecodingException(DecodingException e) {
        String message = e.getMessage();
        responseHeaders.set("Exception", message);
        return ResponseEntity.status(401).headers(responseHeaders)
                .body(new BaseSuccessResponse(ErrorCodes.UNAUTHORISED.getCode()));
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<?> FileUploadException(FileUploadException e) {
        String message = e.getMessage();
        responseHeaders.set("Exception", message);
        return ResponseEntity.status(400).headers(responseHeaders)
                .body(new CustomSuccessResponse<>(ErrorCodes.UNKNOWN.getCode(), 0, true));
    }

}
