package com.acmeair.hystrix;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Primary
@Service
@Profile("test")
class TestUserCommand extends SpringCloudUserCommand {
    private final String customerServiceAddress;
    
    TestUserCommand(@Value("${customer.service.address}") String customerServiceAddress) {
        this.customerServiceAddress = customerServiceAddress;
    }
    
    @Override
    protected String customerServiceAddress() {
        return customerServiceAddress;
    }
}
