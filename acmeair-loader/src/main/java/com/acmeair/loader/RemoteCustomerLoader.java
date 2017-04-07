package com.acmeair.loader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
class RemoteCustomerLoader implements CustomerLoader {
    private final String remoteUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    RemoteCustomerLoader(@Value("${customer.service.address}") String remoteUrl) {
        this.remoteUrl = remoteUrl;

        restTemplate.setErrorHandler(new ResponseErrorHandler() {
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
                remoteUrl + "/rest/info/loader/load?number={numberOfCustomers}",
                null,
                String.class,
                numCustomers
        );
    }
}
