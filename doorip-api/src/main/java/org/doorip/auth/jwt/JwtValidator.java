package org.doorip.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import lombok.RequiredArgsConstructor;
import org.doorip.exception.UnauthorizedException;
import org.doorip.message.ErrorMessage;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtValidator {
    private final JwtGenerator jwtGenerator;

    public void validateAccessToken(String accessToken) {
        try {
            Claims claims = parseToken(accessToken);
            String issuer = claims.getIssuer();
            if (issuer.equals(JwtType.RT.toString())) {
                throw new UnauthorizedException(ErrorMessage.INVALID_ACCESS_TOKEN_VALUE);
            }
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(ErrorMessage.EXPIRED_ACCESS_TOKEN);
        } catch (Exception e) {
            throw new UnauthorizedException(ErrorMessage.INVALID_ACCESS_TOKEN_VALUE);
        }
    }

    public void validateRefreshToken(String refreshToken) {
        try {
            Claims claims = parseToken(refreshToken);
            String issuer = claims.getIssuer();
            if (issuer.equals(JwtType.AT.toString())) {
                throw new UnauthorizedException(ErrorMessage.INVALID_REFRESH_TOKEN_VALUE);
            }
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(ErrorMessage.EXPIRED_REFRESH_TOKEN);
        } catch (Exception e) {
            throw new UnauthorizedException(ErrorMessage.INVALID_REFRESH_TOKEN_VALUE);
        }
    }

    public void equalsRefreshToken(String refreshToken, String storedRefreshToken) {
        if (!refreshToken.equals(storedRefreshToken)) {
            throw new UnauthorizedException(ErrorMessage.MISMATCH_REFRESH_TOKEN);
        }
    }

    private Claims parseToken(String token) {
        JwtParser jwtParser = jwtGenerator.getJwtParser();
        return jwtParser.parseClaimsJws(token)
                .getBody();
    }
}
