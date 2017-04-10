package com.acmeair;

import com.acmeair.loader.CustomerLoaderREST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;


@SpringBootApplication
public class CustomerServiceApplication extends SpringBootServletInitializer {

    @Autowired
    private CustomerLoaderREST customerLoaderREST;

    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {

        return builder.sources(CustomerServiceApplication.class);
    }
    
    @Override
    public void onStartup(ServletContext container) {
        // we can setup the filter here
    }

    @PostConstruct
    void init() {
        customerLoaderREST.loadCustomers(1);
    }
    
    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
}
