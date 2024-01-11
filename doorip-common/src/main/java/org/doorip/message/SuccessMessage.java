package org.doorip.message;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessMessage {
    /**
     * 200 Ok
     */
    OK(HttpStatus.OK, "s2000", "요청이 성공했습니다."),

    /**
     * 201 Created
     */
    CREATED(HttpStatus.CREATED, "s2010", "요청이 성공했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
