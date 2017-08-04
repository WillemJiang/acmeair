package com.acmeair.hystrix;

import io.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;


@Profile("!SpringCloud")
@Service
class CseAuthenticationCommand extends AuthenticationCommand {

  private static final Logger logger = LoggerFactory.getLogger(CseAuthenticationCommand.class);

  @Value("${customer.service.name:customerServiceApp}")
  private String customerServiceName;

  CseAuthenticationCommand() {
    super(RestTemplateBuilder.create());
  }

  @Override
  protected String getCustomerServiceAddress() {
    return "cse://" + customerServiceName;
  }

  @Override
  protected Object validationRequest(String sessionId) {
    Map<String, String> map = new HashMap<>();
    map.put("sessionId", sessionId);
    return map;
  }
}
