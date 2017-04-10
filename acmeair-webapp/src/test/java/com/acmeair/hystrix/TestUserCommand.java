package com.acmeair.hystrix;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

// Just force spring to use this service for testing
@Primary
@Service
public class TestUserCommand extends UserCommand {
    private final String customerServiceAddress;
    
    TestUserCommand(@Value("${customer.service.address}")String customerServiceAddress) {
        this.customerServiceAddress = customerServiceAddress;
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
                return false;
            }
        
            @Override
            public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
            }
        });
    }
    
    protected String getCustomerServiceAddress() {
        return customerServiceAddress;
    }
    
    
}
