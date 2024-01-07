package org.doorip.user.dto.request;

public record UserSignUpRequest(
        String name,
        String intro,
        String platform
) {
}
