package com.acmeair.hystrix;

import com.acmeair.entities.CustomerSession;
import com.acmeair.service.AuthenticationService;
import com.acmeair.web.dto.CustomerSessionInfo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

public abstract class AuthenticationCommand implements AuthenticationService {

  private static final Logger logger = LoggerFactory.getLogger(AuthenticationCommand.class);
  private final RestTemplate restTemplate;
  @Value("${customer.service.name:customerServiceApp}")
  private String customerServiceName;

  public AuthenticationCommand(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
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

  protected abstract String getCustomerServiceAddress();

  protected abstract Object validationRequest(String sessionId);
}
