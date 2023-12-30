package org.doorip.exception;

import org.doorip.message.ErrorMessage;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException() {
        super(ErrorMessage.UNAUTHORIZED);
    }

    public UnauthorizedException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
