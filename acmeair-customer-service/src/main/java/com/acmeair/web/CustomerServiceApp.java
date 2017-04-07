package com.acmeair.web;


import com.acmeair.config.CustomerConfiguration;
import com.acmeair.loader.CustomerLoaderREST;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;

@Configuration
@ApplicationPath("/rest")
public class CustomerServiceApp extends ResourceConfig {
    public CustomerServiceApp() {
        registerClasses(CustomerREST.class, LoginREST.class, CustomerConfiguration.class, CustomerLoaderREST.class);
        property(ServletProperties.FILTER_FORWARD_ON_404, true);
    }
}   
