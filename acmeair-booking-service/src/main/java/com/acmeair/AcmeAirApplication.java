package com.acmeair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
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
    @ImportResource("classpath*:META-INF/spring/*.bean.xml")
    @ComponentScan(basePackages = {"com.acmeair", "io.servicecomb"})
    class CseConfig {
    }
}
