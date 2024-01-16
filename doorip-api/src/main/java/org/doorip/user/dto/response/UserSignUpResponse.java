package org.doorip.user.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.doorip.auth.jwt.Token;

@Builder(access = AccessLevel.PRIVATE)
public record UserSignUpResponse(
        String accessToken,
        String refreshToken,
        Long userId
) {
    public static UserSignUpResponse of(Token token, Long userId) {
        return UserSignUpResponse.builder()
                .accessToken(token.accessToken())
                .refreshToken(token.refreshToken())
                .userId(userId)
                .build();
    }
}
