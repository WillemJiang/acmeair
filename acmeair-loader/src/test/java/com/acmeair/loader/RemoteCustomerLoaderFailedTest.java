package com.acmeair.loader;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRule;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static com.seanyinx.github.unit.scaffolding.AssertUtils.expectFailing;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class RemoteCustomerLoaderFailedTest {
    @Rule
    public final PactProviderRule providerRule = new PactProviderRule("CustomerService", this);

    private final RemoteCustomerLoader customerLoader = new UnitTestRemoteCustomerLoader(providerRule.getConfig().url());

    @Pact(consumer = "RemoteCustomerLoader")
    public PactFragment createFragment(PactDslWithProvider pactDslWithProvider) {
        return pactDslWithProvider
                .given("Remote customer loader is not available")
                .uponReceiving("a request to load customers")
                .path("/rest/info/loader/load")
                .query("number=5")
                .method("POST")
                .willRespondWith()
                .status(500)
                .toFragment();
    }

    @Test
    @PactVerification
    public void blowsUpWhenRemoteIsNotAvailable() throws IOException {
        try {
            customerLoader.loadCustomers(5);
            expectFailing(RuntimeException.class);
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), is("Remote customer loader returns status code 500"));
        }
    }
}
