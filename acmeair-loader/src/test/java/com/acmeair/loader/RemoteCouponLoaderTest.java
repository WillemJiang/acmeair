package com.acmeair.loader;

import au.com.dius.pact.consumer.PactProviderRule;
import java.io.IOException;
import org.junit.Test;

public class RemoteCouponLoaderTest {

  public final PactProviderRule providerRule = new PactProviderRule("CouponCommandService", this);

  private final RemoteCouponLoader couponLoader = new UnitTestRemoteCouponLoader(providerRule.getConfig().url());

  @Test
  public void requestsRemoteToLoadCoupon() throws IOException {
    couponLoader.loadCoupons(100,0.7);
  }
}
