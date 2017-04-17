package com.acmeair.config;

import com.acmeair.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/info/config")
@Api(value = "Customer Service Configuration Information Service", produces = MediaType.APPLICATION_JSON)
public class CustomerConfiguration {
    @Autowired
    private CustomerService customerService;

    @GET
    @Path("/countCustomers")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get the Customer Resource.", response = Long.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "CustomerCount Resource is found"),
            @ApiResponse(code = 404, message = "CustomerCount Resource cannot be found")
    
    })
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
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get the Customer SessionCount Resource.", response = Long.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "CustomerSessionCount Resource is found"),
        @ApiResponse(code = 404, message = "CustomerSessionCount Resource cannot be found")
    
    })
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
