package org.doorip.openfeign.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${oauth.kakao.name}", url = "${oauth.kakao.url}")
public interface KakaoFeignClient {
    @GetMapping
    KakaoAccessTokenInfo getKakaoAccessTokenInfo(@RequestHeader("Authorization") String accessTokenWithTokenType);
}

