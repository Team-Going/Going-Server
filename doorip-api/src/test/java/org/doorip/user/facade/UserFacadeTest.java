package org.doorip.user.facade;

import lombok.extern.slf4j.Slf4j;
import org.doorip.exception.ConflictException;
import org.doorip.exception.EntityNotFoundException;
import org.doorip.message.ErrorMessage;
import org.doorip.user.dto.request.UserSignInRequest;
import org.doorip.user.dto.request.UserSignUpRequest;
import org.doorip.user.dto.response.UserSignInResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
class UserFacadeTest {
    @Autowired
    UserFacade userFacade;
    @Value("${oauth.kakao.test}")
    String accessToken;

    @AfterEach
    void afterEach() {
        UserSignInRequest request = new UserSignInRequest("kakao");
        try {
            UserSignInResponse response = userFacade.signIn(accessToken, request);
            userFacade.withdraw(response.userId());
        } catch (EntityNotFoundException e) {
            log.error("After Each Error: ", e);
            throw e;
        }
    }

    @DisplayName("동일한 회원의 회원가입 요청이 동시에 여러 개 들어오는 경우 정상적으로 회원가입된다.")
    @Test
    void 동일한_회원의_회원가입_요청이_동시에_여러_개_들어오는_경우_정상적으로_회원가입된다() throws InterruptedException {
        // given
        int threadCount = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        UserSignUpRequest signUpRequest = new UserSignUpRequest("개발자", "동시성 테스트", "kakao");
        UserSignInRequest signInRequest = new UserSignInRequest("kakao");

        // when
        IntStream.range(0, threadCount)
                .forEach(i ->
                        executorService.submit(() -> {
                            try {
                                userFacade.signUp(accessToken, signUpRequest);
                            } catch (ConflictException e) {
                                log.error("Sub Task Thread Error: ", e);
                                throw e;
                            } finally {
                                latch.countDown();
                            }
                        }));
        latch.await();

        // then
        assertThatThrownBy(() -> userFacade.signUp(accessToken, signUpRequest))
                .isInstanceOf(ConflictException.class)
                .hasMessage(ErrorMessage.DUPLICATE_USER.getMessage());
        UserSignInResponse response = userFacade.signIn(accessToken, signInRequest);
        assertThat(response.userId()).isNotNull();
    }
}