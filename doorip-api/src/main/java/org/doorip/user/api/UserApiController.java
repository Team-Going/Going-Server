package org.doorip.user.api;

import lombok.RequiredArgsConstructor;
import org.doorip.auth.UserId;
import org.doorip.common.ApiResponseUtil;
import org.doorip.common.BaseResponse;
import org.doorip.message.SuccessMessage;
import org.doorip.user.dto.request.*;
import org.doorip.user.dto.response.ProfileGetResponse;
import org.doorip.user.dto.response.UserSignInResponse;
import org.doorip.user.dto.response.UserSignUpResponse;
import org.doorip.user.facade.UserFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.doorip.common.Constants.AUTHORIZATION;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@Controller
public class UserApiController implements UserApi {
    private final UserFacade userFacade;

    @GetMapping("/splash")
    @Override
    public ResponseEntity<BaseResponse<?>> splash(@UserId final Long userId) {
        userFacade.splash(userId);
        return ApiResponseUtil.success(SuccessMessage.OK);
    }

    @PostMapping("/signin")
    @Override
    public ResponseEntity<BaseResponse<?>> signIn(@RequestHeader(AUTHORIZATION) final String token,
                                                  @RequestBody final UserSignInRequest request) {
        final UserSignInResponse response = userFacade.signIn(token, request);
        return ApiResponseUtil.success(SuccessMessage.OK, response);
    }

    @PostMapping("/signup")
    @Override
    public ResponseEntity<BaseResponse<?>> signUp(@RequestHeader(AUTHORIZATION) final String token,
                                                  @RequestBody final UserSignUpRequest request) {
        final UserSignUpResponse response = userFacade.signUp(token, request);
        return ApiResponseUtil.success(SuccessMessage.CREATED, response);
    }

    @PatchMapping("/signout")
    @Override
    public ResponseEntity<BaseResponse<?>> signOut(@UserId final Long userId) {
        userFacade.signOut(userId);
        return ApiResponseUtil.success(SuccessMessage.OK);
    }

    @DeleteMapping("/withdraw")
    @Override
    public ResponseEntity<BaseResponse<?>> withdraw(@UserId final Long userId) {
        userFacade.withdraw(userId);
        return ApiResponseUtil.success(SuccessMessage.OK);
    }

    @PostMapping("/reissue")
    @Override
    public ResponseEntity<BaseResponse<?>> reissue(@RequestHeader(AUTHORIZATION) final String refreshtoken,
                                                   @RequestBody final UserReissueRequest request) {
        final UserSignUpResponse response = userFacade.reissue(refreshtoken, request);
        return ApiResponseUtil.success(SuccessMessage.OK, response);
    }

    @GetMapping("/profile")
    @Override
    public ResponseEntity<BaseResponse<?>> getProfile(@UserId final Long userId) {
        final ProfileGetResponse response = userFacade.getProfile(userId);
        return ApiResponseUtil.success(SuccessMessage.OK, response);
    }

    @PatchMapping("/test")
    @Override
    public ResponseEntity<BaseResponse<?>> updateResult(@UserId final Long userId,
                                                        @RequestBody final ResultUpdateRequest request) {
        userFacade.updateResult(userId, request);
        return ApiResponseUtil.success(SuccessMessage.OK);
    }

    @PatchMapping("/profile")
    public ResponseEntity<BaseResponse<?>> updateProfile(@UserId final Long userId,
                                                         @RequestBody final ProfileUpdateRequest request) {
        userFacade.updateProfile(userId, request);
        return ApiResponseUtil.success(SuccessMessage.OK);
    }
}
