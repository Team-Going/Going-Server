package org.doorip.user.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.doorip.exception.InvalidValueException;
import org.doorip.message.ErrorMessage;

import java.util.Arrays;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Platform {
    APPLE("apple"),
    KAKAO("kakao");

    private final String stringPlatform;

    public static Platform getEnumPlatformFromStringPlatform(String stringPlatform) {
        return Arrays.stream(values())
                .filter(platform -> platform.stringPlatform.equals(stringPlatform))
                .findFirst()
                .orElseThrow(() -> new InvalidValueException(ErrorMessage.INVALID_PLATFORM_TYPE));
    }
}