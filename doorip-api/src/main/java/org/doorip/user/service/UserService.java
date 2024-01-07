package org.doorip.user.service;

import lombok.RequiredArgsConstructor;
import org.doorip.auth.jwt.JwtProvider;
import org.doorip.auth.jwt.Token;
import org.doorip.exception.EntityNotFoundException;
import org.doorip.message.ErrorMessage;
import org.doorip.openfeign.apple.AppleOAuthProvider;
import org.doorip.openfeign.kakao.KakaoOAuthProvider;
import org.doorip.user.domain.Platform;
import org.doorip.user.domain.RefreshToken;
import org.doorip.user.domain.User;
import org.doorip.user.dto.request.UserSignInRequest;
import org.doorip.user.dto.response.UserResponse;
import org.doorip.user.repository.RefreshTokenRepository;
import org.doorip.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.doorip.user.domain.Platform.APPLE;
import static org.doorip.user.domain.Platform.getEnumPlatformFromStringPlatform;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;
    private final AppleOAuthProvider appleOAuthProvider;
    private final KakaoOAuthProvider kakaoOAuthProvider;

    public UserResponse signIn(String token, UserSignInRequest request) {
        Platform enumPlatform = getEnumPlatformFromStringPlatform(request.platform());
        String platformId = getPlatformId(token, enumPlatform);
        User findUser = getUser(enumPlatform, platformId);
        Token issueToken = jwtProvider.issueToken(findUser.getId());
        updateRefreshToken(issueToken.refreshToken(), findUser);

        return UserResponse.of(issueToken);
    }

    private String getPlatformId(String token, Platform platform) {
        if (platform == APPLE) {
            return appleOAuthProvider.getApplePlatformId(token);
        }
        return kakaoOAuthProvider.getKakaoPlatformId(token);
    }

    private User getUser(Platform platform, String platformId) {
        return userRepository.findUserByPlatformAndPlatformId(platform, platformId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));
    }

    private void updateRefreshToken(String refreshToken, User user) {
        user.updateRefreshToken(refreshToken);
        refreshTokenRepository.save(RefreshToken.createRefreshToken(user.getId(), refreshToken));
    }
}