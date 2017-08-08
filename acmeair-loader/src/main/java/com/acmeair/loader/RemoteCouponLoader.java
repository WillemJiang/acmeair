package com.acmeair.loader;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

public abstract class RemoteCouponLoader implements CouponLoader {
  private final RestTemplate restTemplate;

  @Value("${coupon.command.service.name:couponCommandServiceApp}")
  String couponCommandServiceName;

  RemoteCouponLoader(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
    this.restTemplate.setErrorHandler(new ResponseErrorHandler() {
      @Override
      public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return !clientHttpResponse.getStatusCode().is2xxSuccessful();
      }

      @Override
      public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        throw new RuntimeException("Remote customer loader returns status code "
            + clientHttpResponse.getStatusCode());
      }
    });
  }

  @Override
  public void loadCoupons(long numTickets, double discount) {
    restTemplate.postForEntity(
        getCouponCommandServiceAddress() + "/info/loader/load?number={numTickets}&discount={discount}",
        null,
        String.class,
        numTickets,
        discount
    );
  }


  protected abstract String getCouponCommandServiceAddress();


}
