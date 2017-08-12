package com.acmeair;

import io.servicecomb.foundation.common.utils.Log4jUtils;
import io.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class AcmeAirGatewayApplication {

    public static void main(String[] args) throws Exception {
        Log4jUtils.init();
        SpringApplication.run(AcmeAirGatewayApplication.class, args);
    }

    @Profile("!SpringCloud")
    @EnableServiceComb
    @Configuration
    static class ServiceCombConfig {

    }
}
