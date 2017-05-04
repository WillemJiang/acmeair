package com.acmeair.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class CustomerValidationRuleConfig {
    @Profile("!cse")
    @Bean
    CustomerValidationRule customerValidationRule() {
        return new CustomerValidationRuleImpl();
    }

    @Profile("cse")
    @Bean
    CustomerValidationRule alwaysPassCustomerValidationRule() {
        return CustomerValidationRule.alwaysPassValidationRule;
    }
}
