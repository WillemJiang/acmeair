package com.acmeair.hystrix;

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
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class AuthenticationCommandValidationTest {
    @Rule
    public final PactProviderRule providerRule = new PactProviderRule("CustomerService", this);

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final CustomerSessionInfo   customerSessionInfo = new CustomerSessionInfo(
        "session-id-434200",
        "sean-123",
        dateOf("2017-04-08"),
        dateOf("2027-04-07")
    );
    private final AuthenticationService userService = new TestAuthenticationCommand(providerRule.getConfig().url());

    @Pact(consumer = "AuthenticationService")
    public PactFragment createFragment(PactDslWithProvider pactDslWithProvider) throws JsonProcessingException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", MediaType.APPLICATION_FORM_URLENCODED);

        return pactDslWithProvider
            .given("Customer Sean is registered")
            .uponReceiving("a request to validate Sean")
            .path("/api/login/validate")
            .method("POST")
            .query("sessionId=" + customerSessionInfo.getId())
            .headers(headers)
            .willRespondWith()
            .status(200)
            .body(objectMapper.writeValueAsString(customerSessionInfo), MediaType.APPLICATION_JSON)
            .toFragment();
    }

    @Test
    @PactVerification
    public void validatesCustomerWithSessionId() throws IOException {
        CustomerSession actual = userService.validateCustomerSession(customerSessionInfo.getId());

        assertThat(actual, is(customerSessionInfo));
    }

    private Date dateOf(String source) {
        try {
            return dateFormat.parse(source);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Wrong date format. Expected is yyyy-MM-dd", e);
        }
    }
}
