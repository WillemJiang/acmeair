package com.acmeair.loader;

import com.acmeair.entities.Coupon;
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
import org.springframework.beans.factory.annotation.Autowired;
import javax.ws.rs.core.MediaType;

@RestSchema(schemaId = "coupon_loader")
@Path("/coupon/loader")
@Api(value = "Coupon Service Coupon Activity Loading Service", produces = MediaType.APPLICATION_JSON)
public class CouponLoaderREST {

  private final CouponCommandService couponService;

  @Autowired
  CouponLoaderREST(CouponCommandService couponService) {
    this.couponService = couponService;
  }

  @GET
  @Path("/create/{number}/{discount}")
  @Produces(MediaType.APPLICATION_JSON)
  public String createCoupon(
      @PathParam("number") String number,
      @PathParam("discount") String discount) {
     Coupon coupon = couponService.createCoupon(Long.parseLong(number), Double.parseDouble(discount));
     return coupon.getCouponId();
  }
}