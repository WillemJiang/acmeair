package com.acmeair.web;

import com.acmeair.config.AcmeAirConfiguration;
import com.acmeair.config.LoaderREST;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("/rest/info")
public class AppConfig extends ResourceConfig {
    public AppConfig() {
        property(ServletProperties.FILTER_FORWARD_ON_404, true);
        registerClasses(LoaderREST.class, AcmeAirConfiguration.class);
    }
}
