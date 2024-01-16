package org.doorip.user.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.doorip.auth.UserId;
import org.doorip.common.BaseResponse;
import org.doorip.user.dto.request.ResultUpdateRequest;
import org.doorip.user.dto.request.UserReissueRequest;
import org.doorip.user.dto.request.UserSignInRequest;
import org.doorip.user.dto.request.UserSignUpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import static org.doorip.common.Constants.AUTHORIZATION;

@Tag(name = "회원 관련 API")
public interface UserApi {
    @Operation(
            summary = "스플래쉬 분기 처리 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "요청이 성공했습니다."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 값이 올바르지 않습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰이 만료되었습니다. 재발급 받아주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "404",
                            description = "존재하지 않는 회원입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "404",
                            description = "유저의 성향 결과 값을 찾을 수 없습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "405",
                            description = "잘못된 HTTP method 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content)})
    ResponseEntity<BaseResponse<?>> splash(@Parameter(hidden = true)
                                           @UserId final Long userId);

    @Operation(
            summary = "로그인 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "요청이 성공했습니다."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "400",
                            description = "유효하지 않은 플랫폼 타입입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "애플 아이덴티티 토큰의 형식이 올바르지 않습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "애플 아이덴티티 토큰의 값이 올바르지 않습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "애플 로그인 중 퍼블릭 키 생성에 문제가 발생했습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "애플 로그인 중 아이덴티티 토큰의 유효 기간이 만료되었습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "애플 아이덴터티 토큰의 클레임 값이 올바르지 않습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "카카오 액세스 토큰의 정보를 조회하는 과정에서 오류가 발생하였습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "404",
                            description = "존재하지 않는 회원입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "405",
                            description = "잘못된 HTTP method 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content)})
    ResponseEntity<BaseResponse<?>> signIn(@RequestHeader(AUTHORIZATION) final String token,
                                           @RequestBody final UserSignInRequest request);

    @Operation(
            summary = "회원가입 API",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "요청이 성공했습니다."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "400",
                            description = "유효하지 않은 플랫폼 타입입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "애플 아이덴티티 토큰의 형식이 올바르지 않습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "애플 아이덴티티 토큰의 값이 올바르지 않습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "애플 로그인 중 퍼블릭 키 생성에 문제가 발생했습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "애플 로그인 중 아이덴티티 토큰의 유효 기간이 만료되었습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "애플 아이덴터티 토큰의 클레임 값이 올바르지 않습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "카카오 액세스 토큰의 정보를 조회하는 과정에서 오류가 발생하였습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "405",
                            description = "잘못된 HTTP method 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "409",
                            description = "이미 존재하는 회입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content)})
    ResponseEntity<BaseResponse<?>> signUp(@RequestHeader(AUTHORIZATION) final String token,
                                           @RequestBody final UserSignUpRequest request);

    @Operation(
            security = @SecurityRequirement(name = "Authorization"),
            summary = "로그아웃 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "요청이 성공했습니다."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 값이 올바르지 않습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰이 만료되었습니다. 재발급 받아주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "404",
                            description = "존재하지 않는 회원입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "405",
                            description = "잘못된 HTTP method 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content)})
    ResponseEntity<BaseResponse<?>> signOut(@Parameter(hidden = true)
                                            @UserId final Long userId);

    @Operation(
            security = @SecurityRequirement(name = "Authorization"),
            summary = "회원 탈퇴 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "요청이 성공했습니다."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 값이 올바르지 않습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰이 만료되었습니다. 재발급 받아주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "405",
                            description = "잘못된 HTTP method 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content)})
    ResponseEntity<BaseResponse<?>> withdraw(@Parameter(hidden = true)
                                             @UserId final Long userId);

    @Operation(
            summary = "JWT 재발급 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "요청이 성공했습니다."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 값이 올바르지 않습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰이 만료되었습니다. 재발급 받아주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "404",
                            description = "존재하지 않는 회원입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "404",
                            description = "리프레쉬 토큰을 찾을 수 없습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "405",
                            description = "잘못된 HTTP method 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content)})
    ResponseEntity<BaseResponse<?>> reissue(@RequestHeader(AUTHORIZATION) final String refreshtoken,
                                            @RequestBody final UserReissueRequest request);

    @Operation(
            security = @SecurityRequirement(name = "Authorization"),
            summary = "프로필 조회 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "요청이 성공했습니다."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 값이 올바르지 않습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰이 만료되었습니다. 재발급 받아주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "405",
                            description = "잘못된 HTTP method 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content)})
    ResponseEntity<BaseResponse<?>> getProfile(@Parameter(hidden = true)
                                               @UserId final Long userId);

    @Operation(
            security = @SecurityRequirement(name = "Authorization"),
            summary = "프로필 성향 테스트 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "요청이 성공했습니다."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "400",
                            description = "유효하지 않은 성향 입력 값입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 값이 올바르지 않습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰이 만료되었습니다. 재발급 받아주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "404",
                            description = "존재하지 않는 회원입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "404",
                            description = "대상을 찾을 수 없습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "405",
                            description = "잘못된 HTTP method 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content)})
    ResponseEntity<BaseResponse<?>> updateResult(@Parameter(hidden = true)
                                                 @UserId final Long userId,
                                                 @RequestBody final ResultUpdateRequest request);
}
