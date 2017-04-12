package com.acmeair.hystrix;

import com.acmeair.service.UserService;
import com.acmeair.web.dto.CustomerInfo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
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
    private static final Logger logger = LoggerFactory.getLogger(UserCommand.class);

    private RestTemplate restTemplate = new RestTemplate();
    
    @Autowired
    private LoadBalancerClient loadBalancer;
    
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
        ResponseEntity<CustomerInfo> resp = restTemplate.getForEntity(getCustomerServiceAddress() + "/rest/api/customer/{custid}", CustomerInfo.class, customerId);
        if (resp.getStatusCode() != HttpStatus.OK) {
            throw new NoSuchElementException("No such customer with id " + customerId);
        }
        return resp.getBody();
    }
    
    protected String getCustomerServiceAddress() {
        String address = loadBalancer.choose(customerServiceName).getUri().toString();
        logger.info("Just get the address {} from LoadBalancer.", address);
        return address;
    }
}
