package com.acmeair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import javax.servlet.ServletContext;


@SpringBootApplication
@EnableZuulProxy
public class AcmAirApplication extends SpringBootServletInitializer {
    
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        
        return builder.sources(AcmAirApplication.class);
    }
    
    @Override
    public void onStartup(ServletContext container) {
        // we can setup the filter here
    }

    public static void main(String[] args) {
        SpringApplication.run(AcmAirApplication.class, args);
    }
}
