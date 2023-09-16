package com.yourink.controllerAdvice;

import com.yourink.dto.api.ApiResponse;
import com.yourink.dto.api.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MethodArgumentNotValidAdvice {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {

        var apiResponse = ApiResponse.createErrorResponse(ErrorCode.BAD_REQUEST.getMessage(), ErrorCode.BAD_REQUEST.getCode());

        ex.getFieldErrors()
                .forEach(fieldError -> apiResponse.addValidation(fieldError.getField(), fieldError.getDefaultMessage()));

        return ResponseEntity
                .badRequest()
                .body(apiResponse);
    }
}
