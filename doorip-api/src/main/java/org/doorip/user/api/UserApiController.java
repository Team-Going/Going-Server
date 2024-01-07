package org.doorip.user.api;

import lombok.RequiredArgsConstructor;
import org.doorip.auth.UserId;
import org.doorip.common.ApiResponse;
import org.doorip.common.ApiResponseUtil;
import org.doorip.message.SuccessMessage;
import org.doorip.user.dto.request.UserSignInRequest;
import org.doorip.user.dto.request.UserSignUpRequest;
import org.doorip.user.dto.response.UserResponse;
import org.doorip.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@Controller
public class UserApiController {
    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<?>> signIn(@RequestHeader("Authorization") final String token,
                                                 @RequestBody final UserSignInRequest request) {
        final UserResponse response = userService.signIn(token, request);
        return ApiResponseUtil.success(SuccessMessage.OK, response);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> signUp(@RequestHeader("Authorization") final String token,
                                                 @RequestBody final UserSignUpRequest request) {
        final UserResponse response = userService.signUp(token, request);
        return ApiResponseUtil.success(SuccessMessage.CREATED, response);
    }

    @PatchMapping("/signout")
    public ResponseEntity<ApiResponse<?>> signOut(@UserId final Long userId) {
        userService.signOut(userId);
        return ApiResponseUtil.success(SuccessMessage.OK);
    }
}
