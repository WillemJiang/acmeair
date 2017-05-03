package com.acmeair.loader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

abstract class RemoteCustomerLoader implements CustomerLoader {

    private final RestTemplate restTemplate;

    @Value("${customer.service.name:customerServiceApp}")
    String customerServiceName;

    RemoteCustomerLoader(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
                return !clientHttpResponse.getStatusCode().is2xxSuccessful();
            }

            @Override
            public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
                throw new RuntimeException("Remote customer loader returns status code "
                                           + clientHttpResponse.getStatusCode());
            }
        });
    }

    @Override
    public void loadCustomers(long numCustomers) {
        restTemplate.postForEntity(
                getCustomerServiceAddress() + "/info/loader/load?number={numberOfCustomers}",
                null,
                String.class,
                numCustomers
        );
    }

    protected abstract String getCustomerServiceAddress();
}
