package com.acmeair.web;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.stereotype.Component;

//@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        // support the static files in Jersey
        property(ServletProperties.FILTER_FORWARD_ON_404, true);
    }
    
}
