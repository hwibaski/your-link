package com.yourink.controllerAdvice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.yourink.dto.api.ApiResponse;
import com.yourink.exception.CustomException;

@RestControllerAdvice
public class CustomExceptionAdvice {
    @ExceptionHandler({CustomException.class})
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(
            CustomException ex) {

        var apiResponse = ApiResponse.createErrorResponse(ex.getMessage(), ex.getCode());

        return ResponseEntity
                .status(ex.getStatus())
                .body(apiResponse);
    }
}
