package com.acmeair.web;


import com.acmeair.config.CustomerConfiguration;
import com.acmeair.loader.CustomerLoaderREST;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;


@Configuration
public class CustomerServiceApp extends ResourceConfig {
    
    @Value("${spring.jersey.application-path:/}")
    private String apiPath;
    
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
        config.setConfigId("AcmeAire-CustomerService");
        config.setTitle("AcmeAire + CustomerService ");
        config.setVersion("v1");
        config.setSchemes(new String[] {"http"});
        config.setBasePath(apiPath);
        config.setResourcePackage("com.acmeair");
        config.setPrettyPrint(true);
        config.setScan(true);
    }
    
    public CustomerServiceApp() {
       registerClasses(CustomerREST.class, LoginREST.class, CustomerConfiguration.class, CustomerLoaderREST.class);
        
    }
}   
