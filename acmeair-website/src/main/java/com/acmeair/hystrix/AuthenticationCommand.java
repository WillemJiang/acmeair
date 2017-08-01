package com.acmeair.hystrix;

import com.acmeair.entities.CustomerSession;
import com.acmeair.service.AuthenticationService;
import com.acmeair.web.dto.CustomerSessionInfo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;


@Service
public class AuthenticationCommand implements AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationCommand.class);

    private RestTemplate restTemplate = RestTemplateBuilder.create();

    @Value("${customer.service.name:customerServiceApp}")
    private String customerServiceName;

    AuthenticationCommand() {
        this.restTemplate.setErrorHandler(new ResponseErrorHandler() {
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
    public CustomerSession validateCustomerSession(String sessionId) {
        ResponseEntity<CustomerSessionInfo> responseEntity = restTemplate.postForEntity(
            getCustomerServiceAddress() + "/api/login/validate",
            validationRequest(sessionId),
            CustomerSessionInfo.class
        );
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            logger.warn("No such customer found with session id {}", sessionId);
            return null;
        }
        return responseEntity.getBody();
    }

    protected String getCustomerServiceAddress() {
        return "cse://" + customerServiceName;
    }

    private HttpEntity<CustomerSessionInfo> validationRequest(String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        CustomerSessionInfo customerSessionInfo = new CustomerSessionInfo();
        customerSessionInfo.setId(sessionId);
        return new HttpEntity<>(customerSessionInfo, headers);
    }
}
