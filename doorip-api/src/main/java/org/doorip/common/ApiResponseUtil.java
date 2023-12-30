package org.doorip.common;

import org.doorip.message.ErrorMessage;
import org.doorip.message.SuccessMessage;
import org.springframework.http.ResponseEntity;

public interface ApiResponseUtil {
    static ResponseEntity<ApiResponse<?>> success(SuccessMessage successMessage) {
        return ResponseEntity.status(successMessage.getHttpStatus())
                .body(ApiResponse.of(successMessage));
    }

    static <T> ResponseEntity<ApiResponse<?>> success(SuccessMessage successMessage, T data) {
        return ResponseEntity.status(successMessage.getHttpStatus())
                .body(ApiResponse.of(successMessage, data));
    }

    static ResponseEntity<ApiResponse<?>> failure(ErrorMessage errorMessage) {
        return ResponseEntity.status(errorMessage.getHttpStatus())
                .body(ApiResponse.of(errorMessage));
    }
}
