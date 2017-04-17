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
package com.acmeair.loader;

import com.acmeair.entities.Customer;
import com.acmeair.entities.Customer.PhoneType;
import com.acmeair.entities.CustomerAddress;
import com.acmeair.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/info/loader")
@Api(value = "Customer Service User Information Loading Service", produces = MediaType.TEXT_PLAIN)
public class CustomerLoaderREST {

	@Autowired
	private CustomerService customerService;

	@POST
	@Path("/load")
	@Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Loading Customer Information.", response = Long.class)
    public Response loadCustomers(@ApiParam(value = "Customer numbers which could be loaded", required = true) @QueryParam("number") long numCustomers) {
		CustomerAddress address = customerService.createAddress("123 Main St.", null, "Anytown", "NC", "USA", "27617");
		for (long ii = 0; ii < numCustomers; ii++) {
			customerService.createCustomer("uid"+ii+"@email.com", "password", Customer.MemberShipStatus.GOLD, 1000000, 1000, "919-123-4567", PhoneType.BUSINESS, address);
		}
		return Response.ok().build();
	}
}
