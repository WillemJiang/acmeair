package com.acmeair.web;

import com.acmeair.entities.Coupon;
import com.acmeair.entities.Ticket;
import com.acmeair.service.CouponQueryService;
import com.acmeair.web.dto.CouponInfo;
import com.acmeair.web.dto.TicketInfo;
import io.servicecomb.provider.rest.common.RestSchema;
import io.swagger.annotations.Api;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;

@RestSchema(schemaId = "coupon_query")
@Path("/coupon/query")
@Api(value = "Coupon Query Service for seckill", produces = MediaType.APPLICATION_JSON)
public class CouponQueryREST {

  private final CouponQueryService couponService;

  @Autowired
  CouponQueryREST(CouponQueryService couponService) {
    this.couponService = couponService;
  }

  @GET
  @Path("/coupon")
  @Produces(MediaType.APPLICATION_JSON)
  public CouponInfo queryCoupon() {
    Coupon coupon = couponService.queryCoupon();
    if (coupon != null) {
      return new CouponInfo(coupon);
    } else {
      return new CouponInfo();
    }
  }

  @GET
  @Path("/tickets/{user}/{onlyunused}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<TicketInfo> queryTickets(@PathParam("user") String user, @PathParam("onlyunused") String onlyUnused) {
    List<Ticket> tickets = couponService.queryTickets(user, Boolean.parseBoolean(onlyUnused));
    List<TicketInfo> results = new ArrayList<>();
    for (Ticket ticket : tickets) {
      results.add(new TicketInfo(ticket));
    }
    return results;
  }
}
