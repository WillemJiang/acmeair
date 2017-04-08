package com.acmeair.hystrix;

import com.acmeair.entities.CustomerSession;
import com.acmeair.service.UserService;
import com.acmeair.web.dto.CustomerInfo;
import com.acmeair.web.dto.CustomerSessionInfo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.NoSuchElementException;


@Service
public class UserCommand implements UserService {
    
    private RestTemplate restTemplate = new RestTemplate();

    private final String customerServiceAddress;

    public UserCommand(@Value("${customer.service.address}") String customerServiceAddress) {
        this.customerServiceAddress = customerServiceAddress;
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
        ResponseEntity<CustomerInfo> resp = restTemplate.getForEntity(customerServiceAddress + "/rest/api/customer/{custid}", CustomerInfo.class, customerId);
        if (resp.getStatusCode() != HttpStatus.OK) {
            throw new NoSuchElementException("No such customer with id " + customerId);
        }
        return resp.getBody();
    }
    
    @HystrixCommand
    public CustomerSession validateCustomerSession(String sessionId) {
        ResponseEntity<CustomerSessionInfo> responseEntity = restTemplate.postForEntity(
                customerServiceAddress + "/rest/api/login/validate",
                validationRequest(sessionId),
                CustomerSessionInfo.class
        );
        return responseEntity.getBody();
    }
    
    private HttpEntity<MultiValueMap<String, String>> validationRequest(String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("sessionId", sessionId);
        
        return new HttpEntity<>(map, headers);
    }
}
