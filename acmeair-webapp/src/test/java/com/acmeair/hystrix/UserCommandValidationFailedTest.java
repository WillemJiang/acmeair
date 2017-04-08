package com.acmeair.hystrix;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRule;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;
import com.acmeair.entities.CustomerSession;
import com.acmeair.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class UserCommandValidationFailedTest {
    @Rule
    public final PactProviderRule providerRule = new PactProviderRule("CustomerService", this);

    private final UserService userService = new UserCommand(providerRule.getConfig().url());
    private final String sessionId = "session-mike-123";

    @Pact(consumer = "UserService")
    public PactFragment createFragment(PactDslWithProvider pactDslWithProvider) throws JsonProcessingException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", MediaType.APPLICATION_FORM_URLENCODED);

        return pactDslWithProvider
                .given("No customer Mike found")
                .uponReceiving("a request to validate Mike")
                .path("/rest/api/login/validate")
                .method("POST")
                .query("sessionId=" + sessionId)
                .headers(headers)
                .willRespondWith()
                .status(200)
                .toFragment();
    }

    @Test
    @PactVerification
    public void getsNullWhenNoCustomerFound() throws IOException {
        CustomerSession customerSession = userService.validateCustomerSession(sessionId);

        assertThat(customerSession, is(nullValue()));
    }
}
