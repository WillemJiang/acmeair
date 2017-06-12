/*******************************************************************************
* Copyright (c) 2013 IBM Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*******************************************************************************/
package com.acmeair.web;

import com.acmeair.entities.Customer;
import com.acmeair.entities.CustomerAddress;
import com.acmeair.service.CustomerService;
import com.acmeair.web.dto.CustomerInfo;
import io.servicecomb.core.exception.InvocationException;
import io.servicecomb.provider.rest.common.RestSchema;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RestSchema(schemaId = "customer_REST")
@Path("/api/customer")
@Api(value = "Customer Information Query and Update Service", produces = MediaType.APPLICATION_JSON)
public class CustomerREST {
    private static final Logger logger = LoggerFactory.getLogger(CustomerREST.class);

    static final String LOGIN_USER = "acmeair.login_user";

    @Autowired
    private CustomerService customerService;

    @Context
    private HttpServletRequest request;

    @Autowired
    private CustomerValidationRule customerValidationRule;

    private int flag = 1;

    private boolean validate(String customerid) {
        return customerValidationRule.validate(customerid, request);
    }

    @GET
    @Path("/byid/{custid}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Get the customer from customer id when the customer is logged.",
            notes = "This can only be done by the logged in user.",
            response = CustomerInfo.class,
            produces = MediaType.APPLICATION_JSON)
    @ApiResponses(
            value = {@ApiResponse(code = 403, message = "Invalid user information"),
                    @ApiResponse(code = 500, message = "CustomerService Internal Server Error")})
    public CustomerInfo getCustomerById(
            @ApiParam(
                    value = "Session id from the cookie",
                    required = true) @CookieParam("sessionid") String sessionid,
            @ApiParam(value = "Customer id", required = true) @PathParam("custid") String customerid) {
        try {
            logger.info("Received request to get customer by id {} with session {}", customerid, sessionid);
            // make sure the user isn't trying to update a customer other than
            // the one currently logged in
            if (!validate(customerid)) {
                logger.info("Customer id mismatched, requested = {}, logged = {}",
                        customerid,
                        request.getHeader("acmeair.login_user"));
                throw new InvocationException(Response.Status.FORBIDDEN, "Forbidden");
            }
            Customer customer = customerService.getCustomerByUsername(customerid);
            CustomerInfo customerDTO = new CustomerInfo(customer);
            return customerDTO;
        } catch (InvocationException e) {
            throw e;
        } catch (Exception e) {
            logger.warn("Failed to get customer information", e);
            throw new InvocationException(Response.Status.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @GET
    @Path("/{custid}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {@ApiResponse(code = 500, message = "CustomerService Internal Server Error")})
    // We should not expose this API to the public (It can be
    public CustomerInfo getCustomer(@PathParam("custid") String customerid) {
        try {
            Customer customer = customerService.getCustomerByUsername(customerid);
            
            long starttime = new Date().getTime();
            //通过预制一个开关，使用udi5登陆的时候，调用次接口可以出发触发升压90s
            if ("uid5@email.com".equals(customerid)) {
                logger.info("Stress Test begin.");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        long time;
                        while (true) {
                            long endtime = new Date().getTime();
                            time = endtime - starttime;
                            if (time / 1000 > 90) {
                                break;
                            }
                            int j = 10000000;
                            while (j > 0) {
                                j--;
                            }

                        }
                        logger.info("Stress Test end, Duration: "+time+"s");

                    }
                }).start();

            } ;
            
          //通过预制一个开关，使用udi3登陆的时候，调用次接口可以触发周期性异常和成功，方便测试治理的能力。
            if ("uid3@email.com".equals(customerid)) {
                int temp = flag % 4;
                logger.error("Exception design,flag : " + flag);
                ++flag;
                if (temp == 1 || temp == 2) {
                    logger.error("go to exception branch." );
                    throw new InvocationException(Response.Status.BAD_REQUEST,
                            "test governance,mistake return error!");

                } else {
                    logger.error("go to right branch." );
                    return new CustomerInfo(customer);
                }

            } else {
                return new CustomerInfo(customer);

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new InvocationException(Response.Status.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @POST
    @Path("/byid/{custid}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Update the customer information",
            produces = MediaType.APPLICATION_JSON,
            response = Customer.class)
    @ApiResponses(value = {@ApiResponse(code = 403, message = "Invalid user information")})
    public Customer putCustomer(@CookieParam("sessionid") String sessionid, CustomerInfo customer) {
        if (!validate(customer.getId())) {
            throw new InvocationException(Response.Status.FORBIDDEN, "Forbidden");
        }

        Customer customerFromDB = customerService.getCustomerByUsernameAndPassword(customer.getId(),
                customer.getPassword());
        if (customerFromDB == null) {
            // either the customer doesn't exist or the password is wrong
            throw new InvocationException(Response.Status.FORBIDDEN, "Forbidden");
        }

        CustomerAddress addressFromDB = customerFromDB.getAddress();
        addressFromDB.setStreetAddress1(customer.getAddress().getStreetAddress1());
        if (customer.getAddress().getStreetAddress2() != null) {
            addressFromDB.setStreetAddress2(customer.getAddress().getStreetAddress2());
        }
        addressFromDB.setCity(customer.getAddress().getCity());
        addressFromDB.setStateProvince(customer.getAddress().getStateProvince());
        addressFromDB.setCountry(customer.getAddress().getCountry());
        addressFromDB.setPostalCode(customer.getAddress().getPostalCode());

        customerFromDB.setPhoneNumber(customer.getPhoneNumber());
        customerFromDB.setPhoneNumberType(Customer.PhoneType.valueOf(customer.getPhoneNumberType()));

        customerService.updateCustomer(customerFromDB);
        customerFromDB.setPassword(null);

        return customerFromDB;
    }

}
