package com.acmeair;

import io.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class AcmeAirApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcmeAirApplication.class, args);
    }

    @Configuration
    @Profile("!cse")
    @EnableDiscoveryClient
    class ConsulServiceDiscoveryConfig {
    }

    @Configuration
    @Profile("cse")
    @EnableServiceComb
    class CseConfig {
    }
}
