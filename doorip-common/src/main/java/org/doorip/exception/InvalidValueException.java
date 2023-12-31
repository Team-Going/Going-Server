package org.doorip.exception;

import org.doorip.message.ErrorMessage;

public class InvalidValueException extends BusinessException {
    public InvalidValueException() {
        super(ErrorMessage.BAD_REQUEST);
    }

    public InvalidValueException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
