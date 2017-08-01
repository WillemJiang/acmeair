package com.acmeair;

import io.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableZuulProxy
public class AcmeAirGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcmeAirGatewayApplication.class, args);
    }

    @EnableServiceComb
    @EnableDiscoveryClient
    @Configuration
    static class ServiceCombConfig {

    }
}
