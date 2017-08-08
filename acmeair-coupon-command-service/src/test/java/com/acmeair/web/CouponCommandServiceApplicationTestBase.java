package com.acmeair.web;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

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
public class CouponCommandServiceApplicationTestBase {

  private static ConfigurableApplicationContext applicationContext;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ConfigurableApplicationContext context;

  @Test
  public void canCreateCoupon() {
    HttpHeaders headers = headers();
    ResponseEntity<String> createCouponEntity = restTemplate.exchange(
        "/rest/coupon/loader/create/{number}/{discount}",
        GET,
        new HttpEntity<String>(headers),
        String.class,
        "100",
        "0.7");

    assertThat(createCouponEntity.getStatusCode(), is(OK));
    assertThat(createCouponEntity.getBody().length(),is(36));
  }

  @Test
  public void canSeckillAfterCouponStart() {
    HttpHeaders headers = headers();
    ResponseEntity<String> createCouponEntity = restTemplate.exchange(
        "/rest/coupon/loader/create/{number}/{discount}",
        GET,
        new HttpEntity<String>(headers),
        String.class,
        "10",
        "0.7");

    assertThat(createCouponEntity.getStatusCode(), is(OK));
    assertThat(createCouponEntity.getBody().length(),is(36));

    ResponseEntity<Boolean> seckillCouponEntity = restTemplate.exchange(
        "/rest/coupon/command/seckill/{user}",
        GET,
        new HttpEntity<String>(headers),
        Boolean.class,
        "zyy");

    assertThat(seckillCouponEntity.getStatusCode(), is(OK));
    assertThat(seckillCouponEntity.getBody(), is(true));

  }

  @Test
  public void seckillFailedAfterCouponKillOver() {

    int ticketcount = 5;

    HttpHeaders headers = headers();
    ResponseEntity<String> createCouponEntity = restTemplate.exchange(
        "/rest/coupon/loader/create/{number}/{discount}",
        GET,
        new HttpEntity<String>(headers),
        String.class,
        ticketcount,
        "0.7");

    assertThat(createCouponEntity.getStatusCode(), is(OK));
    assertThat(createCouponEntity.getBody().length(),is(36));

    for(int i = 0;i < ticketcount;i++)
    {
      ResponseEntity<Boolean> seckillCouponEntity = restTemplate.exchange(
          "/rest/coupon/command/seckill/{user}",
          GET,
          new HttpEntity<String>(headers),
          Boolean.class,
          "zyy");

      assertThat(seckillCouponEntity.getStatusCode(), is(OK));
      assertThat(seckillCouponEntity.getBody(), is(true));
    }

    //over kill
    ResponseEntity<Boolean> seckillCouponEntity = restTemplate.exchange(
        "/rest/coupon/command/seckill/{user}",
        GET,
        new HttpEntity<String>(headers),
        Boolean.class,
        "zyy");

    assertThat(seckillCouponEntity.getStatusCode(), is(OK));
    assertThat(seckillCouponEntity.getBody(), is(false));

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
