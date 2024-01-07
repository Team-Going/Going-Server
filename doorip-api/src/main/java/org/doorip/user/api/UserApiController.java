package org.doorip.user.api;

import lombok.RequiredArgsConstructor;
import org.doorip.common.ApiResponse;
import org.doorip.common.ApiResponseUtil;
import org.doorip.message.SuccessMessage;
import org.doorip.user.dto.request.UserSignInRequest;
import org.doorip.user.dto.response.UserResponse;
import org.doorip.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserApiController {
    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<?>> signIn(@RequestHeader("Authorization") final String token,
                                                 @RequestParam final UserSignInRequest request) {
        final UserResponse response = userService.signIn(token, request);
        return ApiResponseUtil.success(SuccessMessage.OK, response);
    }
}
