package org.doorip.user.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.doorip.auth.jwt.Token;

@Builder(access = AccessLevel.PRIVATE)
public record UserSignInResponse(
        String accessToken,
        String refreshToken,
        boolean isResult,
        Long userId
) {
    public static UserSignInResponse of(Token token, boolean isResult, Long userId) {
        return UserSignInResponse.builder()
                .accessToken(token.accessToken())
                .refreshToken(token.refreshToken())
                .isResult(isResult)
                .userId(userId)
                .build();
    }
}
