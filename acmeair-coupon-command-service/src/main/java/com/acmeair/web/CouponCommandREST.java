package com.acmeair.web;

import com.acmeair.service.CouponCommandService;
import io.servicecomb.provider.rest.common.RestSchema;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;

@RestSchema(schemaId = "coupon_command")
@Path("/coupon/command")
@Api(value = "Coupon Command Service run seckill", produces = MediaType.APPLICATION_JSON)
public class CouponCommandREST {

  private final CouponCommandService couponService;

  @Autowired
  CouponCommandREST(CouponCommandService couponService) {
    this.couponService = couponService;
  }

  @GET
  @Path("/seckill/{user}")
  @Produces(MediaType.APPLICATION_JSON)
  public boolean doSeckill(@PathParam("user") String user) {
    return couponService.doSeckill(user);
  }
}
