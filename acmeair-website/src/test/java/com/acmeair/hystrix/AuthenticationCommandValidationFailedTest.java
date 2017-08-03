package com.acmeair.hystrix;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRule;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;
import com.acmeair.entities.CustomerSession;
import com.acmeair.service.AuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.HttpStatus;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class AuthenticationCommandValidationFailedTest {
    @Rule
    public final PactProviderRule providerRule = new PactProviderRule("CustomerService", this);

    private final AuthenticationService userService = new TestAuthenticationCommand(providerRule.getConfig().url());
    private final String                sessionId   = "session-mike-123";

    @Pact(consumer = "AuthenticationService")
    public PactFragment createFragment(PactDslWithProvider pactDslWithProvider) throws JsonProcessingException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", MediaType.APPLICATION_FORM_URLENCODED);

        return pactDslWithProvider
            .given("No customer Mike found")
            .uponReceiving("a request to validate Mike")
            .path("/api/login/validate")
            .method("POST")
            .query("sessionId=" + sessionId)
            .headers(headers)
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
