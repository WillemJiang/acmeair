package com.acmeair.hystrix;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Primary
@Service
@Profile("test")
class TestAuthenticationCommand extends AuthenticationCommand {
    private final String customerServiceAddress;

    TestAuthenticationCommand(@Value("${customer.service.address}") String customerServiceAddress) {
        this.customerServiceAddress = customerServiceAddress;
    }
    
    protected String getCustomerServiceAddress() {
        return customerServiceAddress;
    }
    
    
}
