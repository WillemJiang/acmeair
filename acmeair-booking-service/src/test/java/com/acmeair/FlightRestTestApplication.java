package com.acmeair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import javax.servlet.ServletContext;

@SpringBootApplication(scanBasePackages = "com.acmeair.web")
public class FlightRestTestApplication extends SpringBootServletInitializer {

    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {

        return builder.sources(FlightRestTestApplication.class);
    }

    @Override
    public void onStartup(ServletContext container) {
        // we can setup the filter here
    }

    public static void main(String[] args) {
        SpringApplication.run(FlightRestTestApplication.class, args);
    }
}
