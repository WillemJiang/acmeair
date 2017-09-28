package com.acmeair.hystrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Profile("SpringCloud")
public class SpringCloudSeckillCommand extends SeckillCommand {
  private static final Logger logger = LoggerFactory.getLogger(SpringCloudSeckillCommand.class);

  @Autowired
  private LoadBalancerClient loadBalancer;

  SpringCloudSeckillCommand() {
    super(new RestTemplate());
  }

  protected String getSeckillServiceAddress() {
    String address = loadBalancer.choose(seckillServiceName).getUri().toString();
    logger.info("Just get the address {} from LoadBalancer.", address);
    return address + "/rest";
  }
}
