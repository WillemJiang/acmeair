package test.com.acmeair.service;

import com.acmeair.service.CustomerService;
import com.acmeair.service.KeyGenerator;
import com.acmeair.web.CustomerREST;
import com.acmeair.web.LoginREST;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;


@SpringBootApplication
public class CustomerRestApplication extends SpringBootServletInitializer {

    private final KeyGenerator    keyGenerator    = Mockito.mock(KeyGenerator.class);
    private final CustomerService customerService = Mockito.mock(CustomerService.class);

    public static void main(String[] args) {
        SpringApplication.run(CustomerRestApplication.class, args);
    }

    @Bean
    KeyGenerator keyGenerator() {
        return keyGenerator;
    }

    @Bean
    CustomerService customerService() {
        return customerService;
    }

    @Configuration
    @ApplicationPath("/rest")
    public static class CustomerRestApp extends ResourceConfig {
        public CustomerRestApp() {
            registerClasses(CustomerREST.class, LoginREST.class);
            property(ServletProperties.FILTER_FORWARD_ON_404, true);
        }
    }
}
