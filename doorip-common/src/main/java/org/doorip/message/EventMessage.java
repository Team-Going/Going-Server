package org.doorip.message;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum EventMessage {
    SIGN_UP_EVENT("doorip 서비스에 회원가입 이벤트가 발생했습니다. 🎉");

    private final String message;
}
