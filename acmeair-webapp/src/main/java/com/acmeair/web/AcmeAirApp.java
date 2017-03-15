package com.acmeair.web;


import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

// Current only support one resource configuration
//@Component
@ApplicationPath("/rest/api")
public class AcmeAirApp extends ResourceConfig {
    public AcmeAirApp() {
        registerClasses(BookingsREST.class, CustomerREST.class, FlightsREST.class, LoginREST.class);
    }
}   
