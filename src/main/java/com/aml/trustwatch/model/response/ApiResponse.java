package com.aml.trustwatch.model.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ApiResponse<T>(
    T data,
    ApiMeta meta,
    List<ApiError> errors
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
            data,
            new ApiMeta(Instant.now().toString(), UUID.randomUUID().toString()),
            List.of()
        );
    }

    public static <T> ApiResponse<T> error(List<ApiError> errors) {
        return new ApiResponse<>(
            null,
            new ApiMeta(Instant.now().toString(), UUID.randomUUID().toString()),
            errors
        );
    }
}

