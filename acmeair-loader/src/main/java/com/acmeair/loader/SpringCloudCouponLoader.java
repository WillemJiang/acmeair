package com.acmeair.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Profile("SpringCloud")
public class SpringCloudCouponLoader extends RemoteCouponLoader {
  private static final Logger logger = LoggerFactory.getLogger(SpringCloudCouponLoader.class);

  @Autowired
  private LoadBalancerClient loadBalancer;

  SpringCloudCouponLoader() {
    super(new RestTemplate());
  }

  protected String getCouponCommandServiceAddress() {
    return couponServiceAddress() + "/rest";
  }

  protected String couponServiceAddress() {
    String address = loadBalancer.choose(couponCommandServiceName).getUri().toString();
    logger.info("Just get the address {} from LoadBalancer.", address);
    return address;
  }
}
