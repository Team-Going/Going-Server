package org.doorip.user.dto.response;

import org.doorip.auth.jwt.Token;

public record UserSignInResponse(
        String accessToken,
        String refreshToken,
        boolean isResult
) {

    public static UserSignInResponse of(Token token, boolean isResult) {
        return new UserSignInResponse(
                token.accessToken(),
                token.refreshToken(),
                isResult
        );
    }
}
