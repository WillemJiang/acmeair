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

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.acmeair.entities.CustomerSession;
import com.acmeair.entities.TokenInfo;
import com.acmeair.service.CustomerService;
import com.acmeair.web.dto.CustomerSessionInfo;

import io.servicecomb.provider.rest.common.RestSchema;
import io.servicecomb.swagger.invocation.exception.InvocationException;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestSchema(schemaId = "login")
@Path("/api/login")
public class LoginREST {
    public static final Logger logger = LoggerFactory.getLogger("LoginREST");

    @Autowired
    private CustomerService customerService;

    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces(MediaType.APPLICATION_JSON)
    @ApiResponses(value = { @ApiResponse(code = 403, message = "Invalid user information"),@ApiResponse(code = 500, message = "CustomerService Internal Server Error") })
    public TokenInfo login(@FormParam("login") String login, @FormParam("password") String password) {
        logger.info("Received login request of username [{}]", login);
        try {
            boolean validCustomer = customerService.validateCustomer(login, password);

            if (!validCustomer) {
                logger.info("No such user exists with username [{}]", login);
                throw new InvocationException(Status.FORBIDDEN, "username or password is wrong!");
            }

            logger.info("Validated user [{}] successfully", login);
            CustomerSession session = customerService.createSession(login);
            // TODO:  Need to fix the security issues here - they are pretty gross likely
            //                NewCookie sessCookie = new NewCookie(SESSIONID_COOKIE_NAME, session.getId(), "/", null, null,
            //                        NewCookie.DEFAULT_MAX_AGE, false);
            logger.info("Generated cookie {} for user {}", session.getId(), login);
            // TODO: The mobile client app requires JSON in the response.
            // To support the mobile client app, choose one of the following designs:
            // - Change this method to return JSON, and change the web app javascript to handle a JSON response.
            //   example:  return Response.ok("{\"status\":\"logged-in\"}").cookie(sessCookie).build();
            // - Or create another method which is identical to this one, except returns JSON response.
            //   Have the web app use the original method, and the mobile client app use the new one.
            return new TokenInfo(session.getId());
        }
        catch (Exception e) {
            throw new InvocationException(Status.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @GET
    @Path("/logout")
    @Produces("text/plain")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "CustomerService Internal Server Error") })
    public String logout(@QueryParam("login") String login, @CookieParam("sessionid") String sessionid) {
        try {
            customerService.invalidateSession(sessionid);
            // The following call will trigger query against all partitions, disable for now
            //          customerService.invalidateAllUserSessions(login);

            // TODO:  Want to do this with setMaxAge to zero, but to do that I need to have the same path/domain as cookie
            // created in login.  Unfortunately, until we have a elastic ip and domain name its hard to do that for "localhost".
            // doing this will set the cookie to the empty string, but the browser will still send the cookie to future requests
            // and the server will need to detect the value is invalid vs actually forcing the browser to time out the cookie and
            // not send it to begin with
            //                NewCookie sessCookie = new NewCookie(SESSIONID_COOKIE_NAME, "");
            return "logged out";
        }
        catch (Exception e) {
            throw new InvocationException(Status.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @POST
    @Path("/validate")
    @Consumes({"application/x-www-form-urlencoded"})
    @Produces(MediaType.APPLICATION_JSON)
    @ApiResponses(value = { @ApiResponse(code = 403, message = "Invalid user information")})
    public CustomerSessionInfo validateCustomer(@FormParam("sessionId") String sessionId) {
        logger.info("Received customer validation request with session id {}", sessionId);
        CustomerSession customerSession = customerService.validateSession(sessionId);
        if (customerSession != null) {
            logger.info("Found customer session {}", customerSession);
            return new CustomerSessionInfo(customerSession);
        }
        throw new InvocationException(Status.FORBIDDEN, "invalid token");
    }

}
