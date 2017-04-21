package com.acmeair.hystrix;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRule;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;
import com.acmeair.entities.Customer;
import com.acmeair.service.UserService;
import com.acmeair.web.dto.AddressInfo;
import com.acmeair.web.dto.CustomerInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class UserCommandTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CustomerInfo customerInfo = new CustomerInfo(
            "sean-123",
            "password",
            Customer.MemberShipStatus.PLATINUM.name(),
            1000,
            356,
            new AddressInfo(
                    "White Plains",
                    "Avenue 1",
                    "New York City",
                    "New York",
                    "USA",
                    "100010"
            ),
            "100-100-111",
            Customer.PhoneType.MOBILE.name()
    );

    private final UserService userService = new TestUserCommand("http://localhost:8082");

    @Pact(consumer = "UserService")
    public PactFragment createFragment(PactDslWithProvider pactDslWithProvider) throws JsonProcessingException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        return pactDslWithProvider
                .given("Customer Sean is registered")
                .uponReceiving("a request for Sean")
                .path("/rest/api/customer/" + customerInfo.getUsername())
                .method("GET")
                .willRespondWith()
                .headers(headers)
                .status(200)
                .body(objectMapper.writeValueAsString(customerInfo))
                .toFragment();
    }

    @Test
    @PactVerification
    public void fetchesExpectedCustomer() throws IOException {
        CustomerInfo actual = userService.getCustomerInfo(customerInfo.getUsername());

        assertThat(actual, is(customerInfo));
    }
}
