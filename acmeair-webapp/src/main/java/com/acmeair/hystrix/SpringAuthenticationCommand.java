package com.acmeair.hystrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
@Profile("SpringCloud")
class SpringAuthenticationCommand extends AuthenticationCommand {

  private static final Logger logger = LoggerFactory.getLogger(AuthenticationCommand.class);

  @Autowired
  private LoadBalancerClient loadBalancer;

  SpringAuthenticationCommand() {
    super(new RestTemplate());
  }

  @Override
  protected String getCustomerServiceAddress() {
    String address = loadBalancer.choose("customerServiceApp").getUri().toString();
    logger.info("Just get the address {} from LoadBalancer.", address);
    return address + "/rest";
  }

  @Override
  protected Object validationRequest(String sessionId) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("sessionId", sessionId);

    return new HttpEntity<>(map, headers);
  }
}
