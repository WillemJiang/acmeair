package com.acmeair.loader;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRule;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

public class RemoteCustomerLoaderTest {
    @Rule
    public final PactProviderRule providerRule = new PactProviderRule("CustomerService", this);

    private final RemoteCustomerLoader customerLoader = new RemoteCustomerLoader(providerRule.getConfig().url());

    @Pact(consumer = "RemoteCustomerLoader")
    public PactFragment createFragment(PactDslWithProvider pactDslWithProvider) throws JsonProcessingException {
        return pactDslWithProvider
                .given("Remote customer loader is available")
                .uponReceiving("a request to load customers")
                .path("/rest/info/loader/load")
                .query("number=5")
                .method("POST")
                .willRespondWith()
                .status(200)
                .toFragment();
    }

    @Test
    @PactVerification
    public void requestsRemoteToLoadCustomers() throws IOException {
        customerLoader.loadCustomers(5);
    }
}
