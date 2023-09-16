package com.yourink.dto.api;

import java.util.HashMap;
import java.util.Map;

public record ApiResponse<T>(boolean success, String message, String code, T data, Map<String, String> validation) {
    public static <K> ApiResponse<K> success(String message, K data) {
        return new ApiResponse<>(true, message, "OK", data, new HashMap<>());
    }

    public static ApiResponse<?> createErrorResponse(String message, String code) {
        return new ApiResponse<>(false, message, code, null, new HashMap<>());
    }

    public void addValidation(String field, String message) {
        this.validation.put(field, message);
    }
}
