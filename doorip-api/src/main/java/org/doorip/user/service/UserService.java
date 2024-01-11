package org.doorip.user.service;

import lombok.RequiredArgsConstructor;
import org.doorip.auth.jwt.JwtProvider;
import org.doorip.auth.jwt.JwtValidator;
import org.doorip.auth.jwt.Token;
import org.doorip.exception.ConflictException;
import org.doorip.exception.EntityNotFoundException;
import org.doorip.exception.InvalidValueException;
import org.doorip.exception.UnauthorizedException;
import org.doorip.message.ErrorMessage;
import org.doorip.openfeign.apple.AppleOAuthProvider;
import org.doorip.openfeign.kakao.KakaoOAuthProvider;
import org.doorip.user.domain.MappingIndex;
import org.doorip.user.domain.Platform;
import org.doorip.user.domain.RefreshToken;
import org.doorip.user.domain.User;
import org.doorip.user.dto.request.ResultUpdateRequest;
import org.doorip.user.dto.request.UserReissueRequest;
import org.doorip.user.dto.request.UserSignInRequest;
import org.doorip.user.dto.request.UserSignUpRequest;
import org.doorip.user.dto.response.ProfileGetResponse;
import org.doorip.user.dto.response.UserResponse;
import org.doorip.user.dto.response.UserSignInResponse;
import org.doorip.user.repository.RefreshTokenRepository;
import org.doorip.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.doorip.user.domain.Platform.APPLE;
import static org.doorip.user.domain.Platform.getEnumPlatformFromStringPlatform;
import static org.doorip.user.domain.RefreshToken.createRefreshToken;
import static org.doorip.user.domain.User.createUser;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;
    private final JwtValidator jwtValidator;
    private final AppleOAuthProvider appleOAuthProvider;
    private final KakaoOAuthProvider kakaoOAuthProvider;

    @Transactional(readOnly = true)
    public void splash(Long userId) {
        User findUser = getUser(userId);
        validateCompletedPropensityTest(findUser);
    }

    public UserSignInResponse signIn(String token, UserSignInRequest request) {
        Platform enumPlatform = getEnumPlatformFromStringPlatform(request.platform());
        String platformId = getPlatformId(token, enumPlatform);
        User findUser = getUser(enumPlatform, platformId);
        boolean isResult = hasResult(findUser);
        Token issueToken = jwtProvider.issueToken(findUser.getId());
        updateRefreshToken(issueToken.refreshToken(), findUser);
        return UserSignInResponse.of(issueToken, isResult);
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

    @Transactional(readOnly = true)
    public ProfileGetResponse getProfile(Long userId) {
        User findUser = getUser(userId);
        return ProfileGetResponse.of(findUser);
    }

    public void updateResult(Long userId, ResultUpdateRequest request) {
        User findUser = getUser(userId);
        validateResult(request);
        List<MappingIndex> mappedIndex = mappingIndex(request.result());
        String resultA = oneTypeResult(mappedIndex.subList(0,3), "S", "A");
        String resultB = oneTypeResult(mappedIndex.subList(3,6), "R", "E");
        String resultC = oneTypeResult(mappedIndex.subList(6,9), "P", "I");

        findUser.updateResult(resultA + resultB + resultC);
    }

    private void validateCompletedPropensityTest(User findUser) {
        if (findUser.getResult() == null) {
            throw new EntityNotFoundException(ErrorMessage.RESULT_NOT_FOUND);
        }
    }

    private User getUser(Platform platform, String platformId) {
        return userRepository.findUserByPlatformAndPlatformId(platform, platformId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));
    }

    private boolean hasResult(User user) {
        return user.getResult() != null;
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

    private User saveUser(UserSignUpRequest request, String platformId, Platform enumPlatform) {
        User user = createUser(request.name(), request.intro(), platformId, enumPlatform);
        return userRepository.save(user);
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

    private void updateRefreshToken(String refreshToken, User user) {
        user.updateRefreshToken(refreshToken);
        refreshTokenRepository.save(createRefreshToken(user.getId(), refreshToken));
    }

    private String getRefreshToken(Long userId) {
        try {
            return getRefreshTokenFromRedis(userId);
        } catch (EntityNotFoundException e) {
            User findUser = getUser(userId);
            return findUser.getRefreshToken();
        }
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));
    }

    private String getRefreshTokenFromRedis(Long userId) {
        RefreshToken storedRefreshToken = refreshTokenRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.REFRESH_TOKEN_NOT_FOUND));
        return storedRefreshToken.getRefreshToken();
    }

    private void validateResult(ResultUpdateRequest request) {
        if (request.result().size() != 9) {
            throw new InvalidValueException(ErrorMessage.INVALID_RESULT_TYPE);
        }
    }

    private List<MappingIndex> mappingIndex(List<Integer> numbers) {
        return numbers.stream()
                .map(this::integerToEnum)
                .collect(Collectors.toList());
    }

    private MappingIndex integerToEnum(Integer value) {
        return switch (value) {
            case 0, 1 -> MappingIndex.FIRST;
            case 2, 3 -> MappingIndex.SECOND;
            default -> throw new InvalidValueException(ErrorMessage.INVALID_RESULT_TYPE);
        };
    }

    private String oneTypeResult(List<MappingIndex> group, String A, String B) {
        return (group.stream().filter(index -> index == MappingIndex.FIRST).count() >= 2) ? A : B;
    }
}
