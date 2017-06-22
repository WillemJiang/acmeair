package com.acmeair.auth;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.acmeair.entities.CustomerSession;
import com.acmeair.service.AcmLoadbalance;
import com.acmeair.service.AuthenticationService;
import com.acmeair.web.dto.CustomerSessionInfo;
import com.netflix.ribbon.proxy.annotation.Hystrix;


public class AuthenticationCommand implements AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationCommand.class);

    private RestTemplate restTemplate = new RestTemplate();


    private AcmLoadbalance loadBalancer;

    @Value("${customer.service.name:customers}")
    private String customerServiceName;

    public AuthenticationCommand() {
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
                return false;
            }

            public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
            }
        });
    }
    
    @Hystrix
    public CustomerSession validateCustomerSession(String sessionId) { 
    	try {
            ResponseEntity<CustomerSessionInfo> responseEntity = restTemplate.postForEntity(
                    getCustomerServiceAddress() + "/api/login/validate",
                    validationRequest(sessionId),
                    CustomerSessionInfo.class
            );
            return responseEntity.getBody();
    	} catch (Exception e) {
    		logger.error("Failed in token validate.");
    		return null;
    	}

        
    }
    
    protected String getCustomerServiceAddress() {
        loadBalancer =  new AcmLoadbalance("customerServiceApp");
        String address = loadBalancer.choose().toString();
        String[] parts = address.split(":");
        address = "http://" + parts[0] + ":" + parts[1];
        logger.info("Just get the address {} from LoadBalancer.", address);
        return address;
    }

    private HttpEntity<MultiValueMap<String, String>> validationRequest(String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("sessionId", sessionId);
        
        return new HttpEntity<MultiValueMap<String, String>>(map, headers);
    }
}
