package com.acmeair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import javax.servlet.ServletContext;

@EnableDiscoveryClient
@SpringBootApplication
public class CustomerServiceApplication extends SpringBootServletInitializer {
    
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {

        return builder.sources(CustomerServiceApplication.class);
    }
    
    @Override
    public void onStartup(ServletContext container) {
        // we can setup the filter here
    }
    
    
    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
}
