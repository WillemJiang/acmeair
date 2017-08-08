package com.acmeair.loader;

import io.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!SpringCloud")
public class CseRemoteCouponLoader extends RemoteCouponLoader {

  CseRemoteCouponLoader() {
    super(RestTemplateBuilder.create());
  }

  @Override
  protected String getCouponCommandServiceAddress() {
    return "cse://" + couponCommandServiceName;
  }
}
