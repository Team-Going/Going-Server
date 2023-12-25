package org.goinggoing;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class HealthCheckApiController {
    @GetMapping
    public String healthCheck() {
        return "going going spring boot server ok!";
    }
}
