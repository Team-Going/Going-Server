package org.doorip.auth.jwt;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtGenerator {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.access-token-expire-time}")
    private long ACCESS_TOKEN_EXPIRE_TIME;
    @Value("${jwt.refresh-token-expire-time}")
    private long REFRESH_TOKEN_EXPIRE_TIME;

    public String generateToken(Long userId, boolean isAccessToken) {
        final Date now = generateNowDate();
        final Date expiration = generateExpirationDate(isAccessToken, now);
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(String.valueOf(userId))
                .setIssuer(setIssuerBy(isAccessToken).toString())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public JwtParser getJwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build();
    }

    private Date generateNowDate() {
        return new Date();
    }

    private Date generateExpirationDate(boolean isAccessToken, Date now) {
        return new Date(now.getTime() + calculateExpirationTime(isAccessToken));
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(encodeSecretKey().getBytes());
    }

    private long calculateExpirationTime(boolean isAccessToken) {
        if (isAccessToken) {
            return ACCESS_TOKEN_EXPIRE_TIME;
        }
        return REFRESH_TOKEN_EXPIRE_TIME;
    }

    private String encodeSecretKey() {
        return Base64.getEncoder()
                .encodeToString(secretKey.getBytes());
    }

    private JwtType setIssuerBy(boolean isAccessToken) {
        if (isAccessToken) {
            return JwtType.AT;
        }
        return JwtType.RT;
    }
}
