package org.doorip.config;

import org.doorip.ExternalRoot;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = ExternalRoot.class)
public class FeignClientConfig {
}
