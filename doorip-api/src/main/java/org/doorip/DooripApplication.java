package org.doorip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackageClasses = {DomainRoot.class, CommonRoot.class, ExternalRoot.class}
)
public class DooripApplication {
    public static void main(String[] args) {
        SpringApplication.run(DooripApplication.class, args);
    }
}
