package com.yourink.dto.api;

import java.util.HashMap;
import java.util.Map;

public record ApiResponse<T>(boolean success, String message, T data, Map<String, String> validation) {

    public static ApiResponse<?> createErrorResponse(String message) {
        return new ApiResponse<>(false, message, null, new HashMap<>());
    }

    public void addValidation(String field, String message) {
        this.validation.put(field, message);
    }
}
