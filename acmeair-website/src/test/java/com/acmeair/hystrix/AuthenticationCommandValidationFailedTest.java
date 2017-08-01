package com.acmeair.hystrix;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRule;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;
import com.acmeair.entities.CustomerSession;
import com.acmeair.service.AuthenticationService;
import com.acmeair.web.dto.CustomerSessionInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.junit.Rule;
import org.junit.Test;

public class AuthenticationCommandValidationFailedTest {
    @Rule
    public final PactProviderRule providerRule = new PactProviderRule("CustomerService", this);

    private final AuthenticationService userService = new TestAuthenticationCommand(providerRule.getConfig().url());
    private final String                sessionId   = "session-mike-123";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Pact(consumer = "AuthenticationService")
    public PactFragment createFragment(PactDslWithProvider pactDslWithProvider) throws JsonProcessingException {
        CustomerSessionInfo sessionInfo = new CustomerSessionInfo();
        sessionInfo.setId(sessionId);

        return pactDslWithProvider
            .given("No customer Mike found")
            .uponReceiving("a request to validate Mike")
            .path("/api/login/validate")
            .body(objectMapper.writeValueAsString(sessionInfo), ContentType.APPLICATION_JSON)
            .method("POST")
            .willRespondWith()
            .status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
            .toFragment();
    }

    @Test
    @PactVerification
    public void getsNullWhenNoCustomerFound() throws IOException {
        CustomerSession customerSession = userService.validateCustomerSession(sessionId);

        assertThat(customerSession, is(nullValue()));
    }
}
