package org.doorip.user.dto.response;

import org.doorip.auth.jwt.Token;

public record UserResponse(
        String accessToken,
        String refreshToken
) {

    public static UserResponse of(Token token) {
        return new UserResponse(
                token.accessToken(),
                token.refreshToken()
        );
    }
}
