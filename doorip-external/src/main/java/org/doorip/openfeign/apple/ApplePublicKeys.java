package org.doorip.openfeign.apple;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.doorip.exception.UnauthorizedException;
import org.doorip.message.ErrorMessage;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ApplePublicKeys {
    private List<ApplePublicKey> keys;

    public ApplePublicKey getMatchesKey(String alg, String kid) {
        return keys.stream()
                .filter(applePublicKey -> applePublicKey.alg().equals(alg) && applePublicKey.kid().equals(kid))
                .findFirst()
                .orElseThrow(() -> new UnauthorizedException(ErrorMessage.INVALID_IDENTITY_TOKEN));
    }
}
