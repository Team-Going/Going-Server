package org.doorip.user.api;

import lombok.RequiredArgsConstructor;
import org.doorip.auth.UserId;
import org.doorip.common.ApiResponse;
import org.doorip.common.ApiResponseUtil;
import org.doorip.message.SuccessMessage;
import org.doorip.user.dto.request.ResultUpdateRequest;
import org.doorip.user.dto.request.UserReissueRequest;
import org.doorip.user.dto.request.UserSignInRequest;
import org.doorip.user.dto.request.UserSignUpRequest;
import org.doorip.user.dto.response.ProfileGetResponse;
import org.doorip.user.dto.response.UserResponse;
import org.doorip.user.dto.response.UserSignInResponse;
import org.doorip.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.doorip.common.Constants.AUTHORIZATION;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@Controller
public class UserApiController {
    private final UserService userService;

    @GetMapping("/splash")
    public ResponseEntity<ApiResponse<?>> splash(@UserId final Long userId) {
        userService.splash(userId);
        return ApiResponseUtil.success(SuccessMessage.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<?>> signIn(@RequestHeader(AUTHORIZATION) final String token,
                                                 @RequestBody final UserSignInRequest request) {
        final UserSignInResponse response = userService.signIn(token, request);
        return ApiResponseUtil.success(SuccessMessage.OK, response);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> signUp(@RequestHeader(AUTHORIZATION) final String token,
                                                 @RequestBody final UserSignUpRequest request) {
        final UserResponse response = userService.signUp(token, request);
        return ApiResponseUtil.success(SuccessMessage.CREATED, response);
    }

    @PatchMapping("/signout")
    public ResponseEntity<ApiResponse<?>> signOut(@UserId final Long userId) {
        userService.signOut(userId);
        return ApiResponseUtil.success(SuccessMessage.OK);
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<ApiResponse<?>> withdraw(@UserId final Long userId) {
        userService.withdraw(userId);
        return ApiResponseUtil.success(SuccessMessage.OK);
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<?>> reissue(@RequestHeader(AUTHORIZATION) final String refreshtoken,
                                                  @RequestBody final UserReissueRequest request) {
        UserResponse response = userService.reissue(refreshtoken, request);
        return ApiResponseUtil.success(SuccessMessage.OK, response);
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<?>> getProfile(@UserId final Long userId) {
        final ProfileGetResponse response = userService.getProfile(userId);
        return ApiResponseUtil.success(SuccessMessage.OK, response);
    }

    @PatchMapping("/test")
    public ResponseEntity<ApiResponse<?>> updateResult(@UserId final Long userId,
                                                       @RequestBody ResultUpdateRequest request) {
        userService.updateResult(userId, request);
        return ApiResponseUtil.success(SuccessMessage.OK);
    }
}
