package com.acmeair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.acmeair.web")
public class AcmAirApplication {
    public static void main(String[] args) {
        SpringApplication.run(AcmAirApplication.class, args);
    }
}
