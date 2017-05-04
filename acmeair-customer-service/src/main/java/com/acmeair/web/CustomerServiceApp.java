package com.acmeair.web;


import com.acmeair.config.CustomerConfiguration;
import com.acmeair.loader.CustomerLoaderREST;
import com.huawei.paas.cse.core.exception.InvocationException;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Configuration
public class CustomerServiceApp extends ResourceConfig {
    
    @Value("${spring.jersey.application-path:/}")
    private String apiPath;
    
    @PostConstruct
    @Profile("cse")
    public void init() {
        // The init method is called
        configureSwagger();
    }

    @PostConstruct
    @Profile("!cse")
    public void initWithSpringCloud() {
        register(CustomerExceptionHandler.class);
        register(LoginResponseFilter.class);

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

    @Provider
    private static class CustomerExceptionHandler implements ExceptionMapper<InvocationException> {

        @Override
        public Response toResponse(InvocationException e) {
            return Response.status(e.getStatus()).entity(e.getErrorData()).build();
        }
    }
}
