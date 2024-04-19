package org.doorip.message;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum EventMessage {
    SIGN_UP_EVENT("[🎉 doorip 회원가입 🎉]\n>> 닉네임: [%s]\n>> 현재 가입자 수: [%d명]");

    private final String message;
}
