package com.acmeair.hystrix;

import com.acmeair.service.UserService;
import com.acmeair.web.dto.CustomerInfo;
import com.huawei.paas.cse.provider.springmvc.reference.RestTemplateBuilder;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.NoSuchElementException;

@Service
public class UserCommand implements UserService {
    private RestTemplate restTemplate = RestTemplateBuilder.create();

    @Value("${customer.service.name:customerServiceApp}")
    private String customerServiceName;

    UserCommand() {
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
            }
        });
    }

    @HystrixCommand
    public CustomerInfo getCustomerInfo(String customerId) {
        String url = String.format("cse://%s/rest/api/customer/{custid}", customerServiceName);
        ResponseEntity<CustomerInfo> resp = restTemplate.getForEntity(url, CustomerInfo.class, customerId);
        if (resp.getStatusCode() != HttpStatus.OK) {
            throw new NoSuchElementException("No such customer with id " + customerId);
        }
        return resp.getBody();
    }

}
