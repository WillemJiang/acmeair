package com.acmeair.morphia.services;

import com.acmeair.entities.Coupon;
import com.acmeair.entities.Ticket;
import com.acmeair.morphia.entities.TicketImpl;
import com.acmeair.morphia.repositories.TicketRepository;
import com.acmeair.service.CouponController;
import com.acmeair.service.CouponQueryService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponQueryServiceImpl extends CouponQueryService {

  @Autowired
  private CouponController couponController;

  @Autowired
  private TicketRepository ticketRepository;

  @Override
  public Coupon queryCoupon() {
    return couponController.getCurrent();
  }

  @Override
  public List<Ticket> queryTickets(String user, boolean onlyUnused) {
    List<TicketImpl> result = ticketRepository.findByUser(user);
    List<Ticket> tickets = new ArrayList<Ticket>();
    for (Ticket ticket : result) {
      if(!onlyUnused || !ticket.getUsed()) {
        tickets.add(ticket);
      }
    }
    return tickets;
  }
}
