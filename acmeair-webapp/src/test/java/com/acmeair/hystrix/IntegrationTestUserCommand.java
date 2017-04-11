package com.acmeair.hystrix;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

// Just force spring to use this service for testing
@Primary
@Service
@Profile("sit")
class IntegrationTestUserCommand extends UserCommand {

    protected String getCustomerServiceAddress() {
        String serviceAddress = super.getCustomerServiceAddress();
        return "http://localhost" + serviceAddress.substring(serviceAddress.lastIndexOf(":"));
    }
}
