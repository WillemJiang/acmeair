package com.acmeair.hystrix;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Primary
@Service
@Profile("test")
class TestUserCommand extends UserCommand {
    private String customerServiceAddress;

    public TestUserCommand() {
    }

    public TestUserCommand(String customerServiceAddress) {
        this.customerServiceAddress = customerServiceAddress;
    }

    protected void setCustomerServiceAddress(String customerServiceAddress) {
        this.customerServiceAddress = customerServiceAddress;
    }

    protected String getCustomerServiceAddress() {
        return customerServiceAddress;
    }

}
