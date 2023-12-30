package org.doorip.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.doorip.message.ErrorMessage;
import org.doorip.message.SuccessMessage;

@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ApiResponse<T> {
    private final int status;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private final String code;
    private final String message;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private final T data;

    public static ApiResponse<?> of(SuccessMessage successMessage) {
        return builder()
                .status(successMessage.getHttpStatus().value())
                .message(successMessage.getMessage())
                .build();
    }

    public static <T> ApiResponse<?> of(SuccessMessage successMessage, T data) {
        return builder()
                .status(successMessage.getHttpStatus().value())
                .message(successMessage.getMessage())
                .data(data)
                .build();
    }

    public static ApiResponse<?> of(ErrorMessage errorMessage) {
        return builder()
                .status(errorMessage.getHttpStatus().value())
                .code(errorMessage.getCode())
                .message(errorMessage.getMessage())
                .build();
    }
}