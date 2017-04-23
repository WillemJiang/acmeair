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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.acmeair.entities.Customer;
import com.acmeair.entities.CustomerAddress;
import com.acmeair.service.*;
import com.acmeair.web.dto.*;
import com.huawei.paas.cse.core.exception.InvocationException;
import com.huawei.paas.cse.provider.rest.common.RestSchema;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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

	private boolean validate(String customerid) {
		String loginUser = request.getHeader(LOGIN_USER);
		return customerid.equals(loginUser);
	}

	@GET
	@Path("/byid/{custid}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get the customer from customer id when the customer is logged.", notes = "This can only be done by the logged in user.", response = CustomerInfo.class, produces = MediaType.APPLICATION_JSON)
	@ApiResponses(value = { @ApiResponse(code = 403, message = "Invalid user information"),@ApiResponse(code = 500, message = "CustomerService Internal Server Error") })
	public CustomerInfo getCustomerById(
			@ApiParam(value = "Session id from the cookie", required = true) @CookieParam("sessionid") String sessionid,
			@ApiParam(value = "Customer id", required = true) @PathParam("custid") String customerid) {
		try {
			logger.info("Received request to get customer by id {} with session {}", customerid, sessionid);
			// make sure the user isn't trying to update a customer other than
			// the one currently logged in
//			if (!validate(customerid)) {
//				logger.info("Customer id mismatched, requested = {}, logged = {}", customerid,
//						request.getHeader("acmeair.login_user"));
//				throw new InvocationException(Response.Status.FORBIDDEN, "Forbidden");
//			}
			Customer customer = customerService.getCustomerByUsername(customerid);
			CustomerInfo customerDTO = new CustomerInfo(customer);
			return customerDTO;
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvocationException(Response.Status.INTERNAL_SERVER_ERROR, "Internal Server Error");
		}
	}

	@GET
	@Path("/{custid}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "CustomerService Internal Server Error") })
	// We should not expose this API to the public (It can be
	public CustomerInfo getCustomer(@PathParam("custid") String customerid) {
		try {
			Customer customer = customerService.getCustomerByUsername(customerid);
			return new CustomerInfo(customer);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvocationException(Response.Status.INTERNAL_SERVER_ERROR, "Internal Server Error");
		}
	}

	@POST
	@Path("/byid/{custid}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update the customer information", produces = MediaType.APPLICATION_JSON, response = Customer.class)
	@ApiResponses(value = { @ApiResponse(code = 403, message = "Invalid user information") })
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
