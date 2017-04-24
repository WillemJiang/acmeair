package com.acmeair.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import com.huawei.paas.cse.provider.springmvc.reference.RestTemplateBuilder;

import java.io.IOException;

@Component
class RemoteCustomerLoader implements CustomerLoader {
    private static final Logger logger = LoggerFactory.getLogger(RemoteCustomerLoader.class);

    private RestTemplate restTemplate = RestTemplateBuilder.create();

    @Autowired
    private LoadBalancerClient loadBalancer;

    @Value("${customer.service.name:customerServiceApp}")
    private String customerServiceName;

    RemoteCustomerLoader() {

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
        String url = String.format("cse://%s/info/loader/load?number={%d}",customerServiceName,numCustomers);
        restTemplate.postForEntity(url,
                null,
                String.class,
                numCustomers
        );
    }

}
