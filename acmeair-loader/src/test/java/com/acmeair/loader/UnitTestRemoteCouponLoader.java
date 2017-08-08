package com.acmeair.loader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Primary
@Component
@Profile({"test", "sit"})
class UnitTestRemoteCouponLoader extends SpringCloudCouponLoader {

  private final String remoteUrl;

  UnitTestRemoteCouponLoader(@Value("${coupon.command.service.address}") String remoteUrl) {
    this.remoteUrl = remoteUrl;
  }

  @Override
  protected String couponServiceAddress() {
    return remoteUrl;
  }
}