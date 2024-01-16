package org.doorip.openfeign.apple;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "${oauth.apple.name}", url = "${oauth.apple.url}")
public interface AppleFeignClient {
    @GetMapping("/keys")
    ApplePublicKeys getApplePublicKeys();
}
