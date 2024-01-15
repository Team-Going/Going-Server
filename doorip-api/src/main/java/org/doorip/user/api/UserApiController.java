package org.doorip.user.api;

import lombok.RequiredArgsConstructor;
import org.doorip.auth.UserId;
import org.doorip.common.BaseResponse;
import org.doorip.common.ApiResponseUtil;
import org.doorip.message.SuccessMessage;
import org.doorip.user.dto.request.ResultUpdateRequest;
import org.doorip.user.dto.request.UserReissueRequest;
import org.doorip.user.dto.request.UserSignInRequest;
import org.doorip.user.dto.request.UserSignUpRequest;
import org.doorip.user.dto.response.ProfileGetResponse;
import org.doorip.user.dto.response.UserSignUpResponse;
import org.doorip.user.dto.response.UserSignInResponse;
import org.doorip.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.doorip.common.Constants.AUTHORIZATION;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@Controller
public class UserApiController implements UserApi {
    private final UserService userService;

    @Override
    @GetMapping("/splash")
    public ResponseEntity<BaseResponse<?>> splash(@UserId final Long userId) {
        userService.splash(userId);
        return ApiResponseUtil.success(SuccessMessage.OK);
    }

    @Override

    @PostMapping("/signin")
    public ResponseEntity<BaseResponse<?>> signIn(@RequestHeader(AUTHORIZATION) final String token,
                                                  @RequestBody final UserSignInRequest request) {
        final UserSignInResponse response = userService.signIn(token, request);
        return ApiResponseUtil.success(SuccessMessage.OK, response);
    }

    @Override

    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<?>> signUp(@RequestHeader(AUTHORIZATION) final String token,
                                                  @RequestBody final UserSignUpRequest request) {
        final UserSignUpResponse response = userService.signUp(token, request);
        return ApiResponseUtil.success(SuccessMessage.CREATED, response);
    }

    @Override
    @PatchMapping("/signout")
    public ResponseEntity<BaseResponse<?>> signOut(@UserId final Long userId) {
        userService.signOut(userId);
        return ApiResponseUtil.success(SuccessMessage.OK);
    }

    @Override

    @DeleteMapping("/withdraw")
    public ResponseEntity<BaseResponse<?>> withdraw(@UserId final Long userId) {
        userService.withdraw(userId);
        return ApiResponseUtil.success(SuccessMessage.OK);
    }

    @Override
    @PostMapping("/reissue")
    public ResponseEntity<BaseResponse<?>> reissue(@RequestHeader(AUTHORIZATION) final String refreshtoken,
                                                   @RequestBody final UserReissueRequest request) {
        final UserSignUpResponse response = userService.reissue(refreshtoken, request);
        return ApiResponseUtil.success(SuccessMessage.OK, response);
    }

    @Override
    @GetMapping("/profile")
    public ResponseEntity<BaseResponse<?>> getProfile(@UserId final Long userId) {
        final ProfileGetResponse response = userService.getProfile(userId);
        return ApiResponseUtil.success(SuccessMessage.OK, response);
    }

    @Override
    @PatchMapping("/test")
    public ResponseEntity<BaseResponse<?>> updateResult(@UserId final Long userId,
                                                        @RequestBody final ResultUpdateRequest request) {
        userService.updateResult(userId, request);
        return ApiResponseUtil.success(SuccessMessage.OK);
    }
}
