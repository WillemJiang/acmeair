package com.acmeair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"com.acmeair"})
public class CustomerServiceApplication {
    
    public static void main(String[] args) {
        if (System.getProperty("spring.profiles.active") == null) {
            System.setProperty("spring.profiles.active", "jpa");
        }
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
}
