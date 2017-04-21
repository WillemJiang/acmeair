package com.acmeair.web;

import com.acmeair.config.AcmeAirConfiguration;
import com.acmeair.config.LoaderREST;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;

@Configuration
@Primary
public class AcmeAirApp extends ResourceConfig {

    @Value("${spring.jersey.application-path:/}")
    private String apiPath;

    public AcmeAirApp() {
        registerClasses(BookingsREST.class, FlightsREST.class);
        registerClasses(LoaderREST.class, AcmeAirConfiguration.class);
        property(ServletProperties.FILTER_FORWARD_ON_404, true);
    }

    @PostConstruct
    public void init() {
        // The init method is called
        configureSwagger();
    }

    private void configureSwagger() {
        register(ApiListingResource.class);
        register(SwaggerSerializers.class);

        // Just setup the configuration of the swagger API
        BeanConfig config = new BeanConfig();
        config.setConfigId("AcmeAire-BookingService");
        config.setTitle("AcmeAire + BookingService ");
        config.setVersion("v1");
        config.setSchemes(new String[] {"http"});
        config.setBasePath(apiPath);
        config.setResourcePackage("com.acmeair");
        config.setPrettyPrint(true);
        config.setScan(true);
    }
}
