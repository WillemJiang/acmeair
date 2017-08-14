package com.acmeair.web;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

import com.acmeair.web.dto.CouponInfo;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CouponQueryServiceApplicationTestBase {
  private static ConfigurableApplicationContext applicationContext;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ConfigurableApplicationContext context;

  @Test
  public void canQueryCoupon() {
    HttpHeaders headers = headers();
    ResponseEntity<CouponInfo> queryCouponEntity = restTemplate.exchange(
        "/rest/coupon/query/coupon",
        GET,
        new HttpEntity<String>(headers),
        CouponInfo.class);

    assertThat(queryCouponEntity.getStatusCode(), is(OK));
  }

  @Test
  public void canQueryTicket() {
    HttpHeaders headers = headers();
    ResponseEntity<String> queryTicketEntity = restTemplate.exchange(
        "/rest/coupon/query/tickets/{user}/{onlyunused}",
        GET,
        new HttpEntity<String>(headers),
        String.class,
        "zyy",
        "false");

    assertThat(queryTicketEntity.getStatusCode(), is(OK));
    System.out.println(queryTicketEntity.getBody());
  }

  @After
  public void tearDown() throws Exception {
    applicationContext = context;
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    if(applicationContext != null){
      applicationContext.close();
    }
  }

  private HttpHeaders headers() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(singletonList(MediaType.APPLICATION_JSON));
    return headers;
  }
}
