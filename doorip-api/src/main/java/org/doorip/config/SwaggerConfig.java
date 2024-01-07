package org.doorip.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI api() {
        Info info = new Info()
                .title("Doorip API Docs")
                .version("v1.0")
                .description("Doorip 서비스 API 명세서 입니다.");
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
