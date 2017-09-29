/*
 *   Copyright 2017 Huawei Technologies Co., Ltd
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.acmeair.web;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.acmeair.morphia.entities.CouponImpl;
import com.acmeair.service.CouponService;

import io.servicecomb.provider.rest.common.RestSchema;
import io.servicecomb.swagger.invocation.exception.InvocationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestSchema(schemaId = "coupon_REST")
@Path("/api/coupon")
@Api(value = "Coupon Information Query and Update Service", produces = MediaType.APPLICATION_JSON)
public class CouponREST {

  private static final Logger logger = LoggerFactory.getLogger(CouponREST.class);

  private final CouponService couponService;

  @Autowired
  CouponREST(CouponService couponService) {
    this.couponService = couponService;
  }

  @GET
  @Path("/sync/{customername}")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "sync coupon from seckill.", notes = "sync coupon from seckill.", produces = MediaType.APPLICATION_JSON)
  @ApiResponses(value = {@ApiResponse(code = 403, message = "Invalid user information"),
      @ApiResponse(code = 500, message = "CustomerService Internal Server Error")})
  public void sync(@ApiParam(value = "customer name", required = true) @PathParam("customername") String customername) {
    try {
      couponService.syncCoupons(customername);
    } catch (Exception e) {
      logger.warn("Failed sync", e);
      throw new InvocationException(Response.Status.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }
  }

  @GET
  @Path("/{customername}")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "query all available coupon.", notes = "query all available coupon.", produces = MediaType.APPLICATION_JSON)
  @ApiResponses(value = {@ApiResponse(code = 403, message = "Invalid user information"),
      @ApiResponse(code = 500, message = "CustomerService Internal Server Error")})
  public List<CouponImpl> query(
      @ApiParam(value = "customer name", required = true) @PathParam("customername") String customername) {
    try {
      return couponService.getCoupons(customername);
    } catch (Exception e) {
      logger.warn("Failed get", e);
      throw new InvocationException(Response.Status.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }
  }

  @PUT
  @Path("/use/{couponid}")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "use coupon.", notes = "use coupon.", produces = MediaType.APPLICATION_JSON)
  @ApiResponses(value = {@ApiResponse(code = 403, message = "Invalid couponid"),
      @ApiResponse(code = 500, message = "CustomerService Internal Server Error")})
  public void use(@ApiParam(value = "coupon id", required = true) @PathParam("couponid") String couponId) {
    try {
      couponService.useCoupon(Integer.parseInt(couponId));
    } catch (Exception e) {
      logger.warn("Failed use", e);
      throw new InvocationException(Response.Status.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }
  }
}
