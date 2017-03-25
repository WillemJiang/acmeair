package com.acmeair.config;

import com.acmeair.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/info/config")
public class CustomerConfiguration {
    @Autowired
    private CustomerService customerService;

    @GET
    @Path("/countCustomers")
    @Produces("application/json")
    public Response countCustomer() {
        try {
            Long customerCount = customerService.count();

            return Response.ok(customerCount).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ok(-1).build();
        }
    }

    @GET
    @Path("/countSessions")
    @Produces("application/json")
    public Response countCustomerSessions() {
        try {
            Long customerCount = customerService.countSessions();

            return Response.ok(customerCount).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ok(-1).build();
        }
    }
}