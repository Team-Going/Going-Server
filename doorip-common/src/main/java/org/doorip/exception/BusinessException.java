package org.doorip.exception;

import lombok.Getter;
import org.doorip.message.ErrorMessage;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorMessage errorMessage;

    public BusinessException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
}
