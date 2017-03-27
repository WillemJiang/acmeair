package com.acmeair.web;


import com.acmeair.config.AcmeAirConfiguration;
import com.acmeair.config.CustomerConfiguration;
import com.acmeair.config.LoaderREST;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.ws.rs.ApplicationPath;

@Configuration
@ApplicationPath("/rest")
@Primary
public class AcmeAirApp extends ResourceConfig {
    public AcmeAirApp() {
        registerClasses(BookingsREST.class, CustomerREST.class, FlightsREST.class, LoginREST.class);
        registerClasses(LoaderREST.class, AcmeAirConfiguration.class, CustomerConfiguration.class);
        property(ServletProperties.FILTER_FORWARD_ON_404, true);
    }
}   
