package com.yourink.controllerAdvice;

import com.yourink.dto.api.ApiResponse;
import com.yourink.dto.api.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class QueryParameterAdvice {
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<ApiResponse<?>> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {

        var apiResponse = ApiResponse.createErrorResponse(ErrorCode.BAD_REQUEST.getMessage(), ErrorCode.BAD_REQUEST.getCode());

        apiResponse.addValidation(ex.getParameterName(), "쿼리 스트링 파라미터가 존재하지 않습니다.");

        return ResponseEntity
                .badRequest()
                .body(apiResponse);
    }
}
