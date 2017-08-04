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

import com.acmeair.entities.CustomerSession;
import com.acmeair.service.AuthenticationService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import java.io.IOException;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
@Profile({"!test"})
public class RESTCookieSessionFilter extends ZuulFilter {
	private static final Logger logger = LoggerFactory.getLogger(RESTCookieSessionFilter.class);
	
	static final String         LOGIN_USER  = "acmeair.login_user";
	private static final String LOGIN_PATH  = "/rest/api/login";
	private static final String LOGOUT_PATH = "/rest/api/login/logout";
	private static final String LOADDB_PATH = "/rest/api/loaddb";
	public static final String  CONFIG_PATH = "/info/config/";
	public static final String  LOADER_PATH = "/info/loader/";
	
	@Autowired
	private AuthenticationService authenticationService;


	private void doFilter(RequestContext context) throws IOException, ServletException {
		HttpServletRequest request = context.getRequest();

		String path = request.getContextPath() + request.getServletPath();
		if (request.getPathInfo() != null) {
			path = path + request.getPathInfo();
		}
		logger.debug("Get the request path {}", path);
		
		
		if (path.endsWith(LOGIN_PATH) || path.endsWith(LOGOUT_PATH) || path.endsWith(LOADDB_PATH) ||
				path.contains(CONFIG_PATH) || path.contains(LOADER_PATH)) {
			// if logging in, logging out, lookup configuration, or loading the database , let the request flow
			return;
		}
		
		Cookie cookies[] = request.getCookies();
		Cookie sessionCookie = null;
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equals(CustomerSession.SESSIONID_COOKIE_NAME)) {
					sessionCookie = c;
				}
				if (sessionCookie!=null)
					break;
			}
			String sessionId = "";
			if (sessionCookie!=null) // We need both cookie to work
				sessionId= sessionCookie.getValue().trim();
			// did this check as the logout currently sets the cookie value to "" instead of aging it out
			// see comment in LogingREST.java
			if (sessionId.equals("")) {
				logger.warn("Session id is empty");
				setFailedRequest(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
			// Need the URLDecoder so that I can get @ not %40
			CustomerSession cs = getCustomerSession(sessionId);
			if (cs != null) {
				context.addZuulRequestHeader(LOGIN_USER, cs.getCustomerid());
				logger.info("Customer {} validated with session id {}", cs.getCustomerid(), sessionId);
				return;
			}
			else {
				logger.warn("No customer session found with session id {}", sessionId);
				setFailedRequest(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
		}

		logger.warn("No session cookie provided");
		// if we got here, we didn't detect the session cookie, so we need to return 404
		setFailedRequest(HttpServletResponse.SC_FORBIDDEN);

	}

	private CustomerSession getCustomerSession(String sessionId) {
		return authenticationService.validateCustomerSession(sessionId);
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	@Override
	public boolean shouldFilter() {
		HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
		String path = request.getContextPath() + request.getServletPath();
		logger.info("Received request to {}", path);
		return true;
	}

	@Override
	public Object run() {
		RequestContext context = RequestContext.getCurrentContext();
		try {
			if (context.getRequest().getCookies() != null) {
				Arrays.stream(context.getRequest().getCookies())
						.forEach(cookie -> logger.debug("pre {}={}", cookie.getName(), cookie.getValue()));
			}
			doFilter(context);
			return null;
		} catch (IOException | ServletException e) {
			throw new IllegalStateException(e);
		}
	}


	/**
	 * response immediately when request failed.
	 * the previous `response.sendError` method will pass to the next filter,
	 * and can not be used.
	 */
	private void setFailedRequest(int code) {
		RequestContext ctx = RequestContext.getCurrentContext();
		ctx.unset();
		ctx.setResponseStatusCode(code);
	}
}
