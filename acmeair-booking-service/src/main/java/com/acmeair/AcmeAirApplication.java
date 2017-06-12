package com.acmeair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.servicecomb.foundation.common.utils.Log4jUtils;
import io.servicecomb.springboot.starter.provider.EnableServiceComb;

@SpringBootApplication
public class AcmeAirApplication {

    public static void main(String[] args) throws Exception {
        Log4jUtils.init();
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
