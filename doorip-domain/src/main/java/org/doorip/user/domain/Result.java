package org.doorip.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.doorip.exception.InvalidValueException;
import org.doorip.message.ErrorMessage;

import java.util.Arrays;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum Result {
    SRP(0, "SRP"),
    SRI(1, "SRI"),
    SEP(2, "SEP"),
    SEI(3, "SEI"),
    ARP(4, "ARP"),
    ARI(5, "ARI"),
    AEP(6, "AEP"),
    AEI(7, "AEI");

    private final int numResult;
    private final String stringResult;

    public static Result getEnumResultFromStringResult(String stringResult) {
        return Arrays.stream(values())
                .filter(result -> result.stringResult.equals(stringResult))
                .findFirst()
                .orElseThrow(() -> new InvalidValueException(ErrorMessage.INVALID_RESULT_TYPE));
    }
}
