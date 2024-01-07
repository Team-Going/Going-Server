package org.doorip.openfeign.apple;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppleIdentityTokenValidator {
    @Value("${oauth.apple.iss}")
    private String iss;
    @Value("${oauth.apple.client-id}")
    private String clientId;

    public boolean isValidAppleIdentityToken(Claims claims) {
        return claims.getIssuer().contains(iss)
                && claims.getAudience().equals(clientId);
    }
}
