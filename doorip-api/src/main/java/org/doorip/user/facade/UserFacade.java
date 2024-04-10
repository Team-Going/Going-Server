package org.doorip.user.facade;

import lombok.RequiredArgsConstructor;
import org.doorip.common.Constants;
import org.doorip.user.dto.request.*;
import org.doorip.user.dto.response.ProfileGetResponse;
import org.doorip.user.dto.response.UserSignInResponse;
import org.doorip.user.dto.response.UserSignUpResponse;
import org.doorip.user.repository.LettuceLockRepository;
import org.doorip.user.service.UserService;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserFacade {
    private final UserService userService;
    private final LettuceLockRepository lettuceLockRepository;

    public void splash(Long userId) {
        userService.splash(userId);
    }

    public UserSignInResponse signIn(String token, UserSignInRequest request) {
        return userService.signIn(token, request);
    }

    public UserSignUpResponse signUp(String token, UserSignUpRequest request) {
        while (!lettuceLockRepository.lock(token, Constants.SIGN_UP_LOCK)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            return userService.signUp(token, request);
        } finally {
            lettuceLockRepository.unlock(token);
        }
    }

    public void signOut(Long userId) {
        userService.signOut(userId);
    }

    public void withdraw(Long userId) {
        userService.withdraw(userId);
    }

    public UserSignUpResponse reissue(String refreshToken, UserReissueRequest request) {
        return userService.reissue(refreshToken, request);
    }

    public ProfileGetResponse getProfile(Long userId) {
        return userService.getProfile(userId);
    }

    public void updateResult(Long userId, ResultUpdateRequest request) {
        userService.updateResult(userId, request);
    }

    public void updateProfile(Long userId, ProfileUpdateRequest request) {
        userService.updateProfile(userId, request);
    }
}
