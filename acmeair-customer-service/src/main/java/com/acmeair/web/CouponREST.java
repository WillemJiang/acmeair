package com.acmeair.web;

import com.acmeair.morphia.entities.CouponImpl;
import com.acmeair.service.CouponService;
import io.servicecomb.provider.rest.common.RestSchema;
import io.servicecomb.swagger.invocation.exception.InvocationException;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

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
    @ApiResponses(value = {@ApiResponse(code = 403, message = "Invalid user information"), @ApiResponse(code = 500, message = "CustomerService Internal Server Error")})
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
    @ApiResponses(value = {@ApiResponse(code = 403, message = "Invalid user information"), @ApiResponse(code = 500, message = "CustomerService Internal Server Error")})
    public List<CouponImpl> query(@ApiParam(value = "customer name", required = true) @PathParam("customername") String customername) {
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
    @ApiResponses(value = {@ApiResponse(code = 403, message = "Invalid couponid"), @ApiResponse(code = 500, message = "CustomerService Internal Server Error")})
    public void use(@ApiParam(value = "coupon id", required = true) @PathParam("couponid") String couponId) {
        try {
            couponService.useCoupon(Integer.parseInt(couponId));
        } catch (Exception e) {
            logger.warn("Failed use", e);
            throw new InvocationException(Response.Status.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }
}
