package com.acmeair.hystrix;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Primary
@Service
@Profile("test")
class TestAuthenticationCommand extends CseAuthenticationCommand {
    private final String customerServiceAddress;

    TestAuthenticationCommand(@Value("${customer.service.address}") String customerServiceAddress) {
        this.customerServiceAddress = customerServiceAddress;
    }

    @Override
    protected String getCustomerServiceAddress() {
        return customerServiceAddress;
    }

    @Override
    protected Object validationRequest(String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("sessionId", sessionId);

        return new HttpEntity<>(map, headers);
    }
}
