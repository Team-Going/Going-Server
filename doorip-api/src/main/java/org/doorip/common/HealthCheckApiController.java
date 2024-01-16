package org.doorip.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "health check", description = "health check API")
@RequestMapping("/")
@RestController
public class HealthCheckApiController {
    @Operation(summary = "health check API", description = "health check API 입니다.")
    @ApiResponse(
            responseCode = "200",
            description = "요청이 성공했습니다.",
            content = @Content(schema = @Schema(implementation = String.class))
    )
    @GetMapping
    public String healthCheck() {
        return "doorip spring boot server ok!";
    }
}
