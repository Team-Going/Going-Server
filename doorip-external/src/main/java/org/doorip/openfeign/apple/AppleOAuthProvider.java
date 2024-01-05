package org.doorip.openfeign.apple;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.doorip.exception.UnauthorizedException;
import org.doorip.message.ErrorMessage;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class AppleOAuthProvider {
    private final AppleFeignClient appleFeignClient;
    private final AppleIdentityTokenParser appleIdentityTokenParser;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;
    private final AppleIdentityTokenValidator appleIdentityTokenValidator;

    public String getApplePlatformId(String identityToken) {
        Map<String, String> headers = appleIdentityTokenParser.parseHeaders(identityToken);
        ApplePublicKeys applePublicKeys = appleFeignClient.getApplePublicKeys();
        PublicKey publicKey = applePublicKeyGenerator.generatePublicKeyWithApplePublicKeys(headers, applePublicKeys);
        Claims claims = appleIdentityTokenParser.parseWithPublicKeyAndGetClaims(identityToken, publicKey);
        validateClaims(claims);

        return claims.getSubject();
    }

    private void validateClaims(Claims claims) {
        if (!appleIdentityTokenValidator.isValidAppleIdentityToken(claims)) {
            throw new UnauthorizedException(ErrorMessage.INVALID_IDENTITY_TOKEN_CLAIMS);
        }
    }
}
