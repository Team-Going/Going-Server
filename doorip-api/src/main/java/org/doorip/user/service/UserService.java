package org.doorip.user.service;

import lombok.RequiredArgsConstructor;
import org.doorip.auth.jwt.JwtProvider;
import org.doorip.auth.jwt.JwtValidator;
import org.doorip.auth.jwt.Token;
import org.doorip.exception.ConflictException;
import org.doorip.exception.EntityNotFoundException;
import org.doorip.exception.UnauthorizedException;
import org.doorip.message.ErrorMessage;
import org.doorip.openfeign.apple.AppleOAuthProvider;
import org.doorip.openfeign.kakao.KakaoOAuthProvider;
import org.doorip.user.domain.Platform;
import org.doorip.user.domain.RefreshToken;
import org.doorip.user.domain.User;
import org.doorip.user.dto.request.UserReissueRequest;
import org.doorip.user.dto.request.UserSignInRequest;
import org.doorip.user.dto.request.UserSignUpRequest;
import org.doorip.user.dto.response.UserResponse;
import org.doorip.user.repository.RefreshTokenRepository;
import org.doorip.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.doorip.user.domain.Platform.APPLE;
import static org.doorip.user.domain.Platform.getEnumPlatformFromStringPlatform;
import static org.doorip.user.domain.User.createUser;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;
    private final JwtValidator jwtValidator;
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

    public UserResponse signUp(String token, UserSignUpRequest request) {
        Platform enumPlatform = getEnumPlatformFromStringPlatform(request.platform());
        String platformId = getPlatformId(token, enumPlatform);
        validateDuplicateUser(enumPlatform, platformId);
        User savedUser = saveUser(request, platformId, enumPlatform);
        Token issueToken = jwtProvider.issueToken(savedUser.getId());
        updateRefreshToken(issueToken.refreshToken(), savedUser);

        return UserResponse.of(issueToken);
    }

    public void signOut(Long userId) {
        User findUser = getUser(userId);
        deleteRefreshToken(findUser);
    }

    public void withdraw(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional(noRollbackFor = UnauthorizedException.class)
    public UserResponse reissue(String refreshToken, UserReissueRequest request) {
        Long userId = request.userId();
        validateRefreshToken(refreshToken, userId);
        User findUser = getUser(userId);
        Token issueToken = jwtProvider.issueToken(userId);
        updateRefreshToken(issueToken.refreshToken(), findUser);

        return UserResponse.of(issueToken);
    }

    private String getPlatformId(String token, Platform platform) {
        if (platform == APPLE) {
            return appleOAuthProvider.getApplePlatformId(token);
        }
        return kakaoOAuthProvider.getKakaoPlatformId(token);
    }

    private void validateDuplicateUser(Platform platform, String platformId) {
        if (userRepository.existsUserByPlatformAndPlatformId(platform, platformId)) {
            throw new ConflictException(ErrorMessage.DUPLICATE_USER);
        }
    }

    private User getUser(Platform platform, String platformId) {
        return userRepository.findUserByPlatformAndPlatformId(platform, platformId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));
    }

    private void updateRefreshToken(String refreshToken, User user) {
        user.updateRefreshToken(refreshToken);
        refreshTokenRepository.save(RefreshToken.createRefreshToken(user.getId(), refreshToken));
    }

    private User saveUser(UserSignUpRequest request, String platformId, Platform enumPlatform) {
        User user = createUser(request.name(), request.intro(), platformId, enumPlatform);
        return userRepository.save(user);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));
    }

    private void deleteRefreshToken(User user) {
        user.updateRefreshToken(null);
        refreshTokenRepository.deleteById(user.getId());
    }

    private void validateRefreshToken(String refreshToken, Long userId) {
        try {
            jwtValidator.validateRefreshToken(refreshToken);
            String storedRefreshToken = getRefreshToken(userId);
            jwtValidator.equalsRefreshToken(refreshToken, storedRefreshToken);
        } catch (UnauthorizedException e) {
            signOut(userId);
            throw e;
        }
    }

    private String getRefreshToken(Long userId) {
        try {
            return getRefreshTokenFromRedis(userId);
        } catch (EntityNotFoundException e) {
            User findUser = getUser(userId);
            return findUser.getRefreshToken();
        }
    }

    private String getRefreshTokenFromRedis(Long userId) {
        RefreshToken storedRefreshToken = refreshTokenRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.REFRESH_TOKEN_NOT_FOUND));
        return storedRefreshToken.getRefreshToken();
    }
}
