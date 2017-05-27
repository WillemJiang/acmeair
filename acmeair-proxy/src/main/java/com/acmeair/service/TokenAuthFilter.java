package com.acmeair.service;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acmeair.auth.AuthenticationCommand;
import com.acmeair.entities.CustomerSession;

public class TokenAuthFilter implements Filter{
	
	static final String LOGIN_USER = "acmeair.login_user";
	static final String LOGIN = "login";
	static final String LOGINOUT = "loginout";
	private static final String LOGIN_PATH = "/rest/api/login";
	private static final String LOGOUT_PATH = "/rest/api/login/logout";
	private static final String LOADDB_PATH = "/rest/api/loaddb";
 	public static final String  CONFIG_PATH = "/info/config/";
 	public static final String  LOADER_PATH = "/info/loader/";
    private AuthenticationService authenticationService = new AuthenticationCommand();
	private static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthFilter.class);

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		LOGGER.info("TokenAuthFilter begin...");
		HttpServletRequest request = (HttpServletRequest) arg0; 
		HttpServletResponse response = (HttpServletResponse) arg1;
		String path = request.getContextPath() + request.getServletPath();
		if (request.getPathInfo() != null) {
			path = path + request.getPathInfo();
		}

		if (path.endsWith(LOADDB_PATH) ||
 				path.contains(CONFIG_PATH) || path.contains(LOADER_PATH)) {
			arg2.doFilter(arg0, arg1);
			return;
		}

		
		if (path.endsWith(LOGIN_PATH) ) {
			request.setAttribute(LOGIN, "true");
			arg2.doFilter(arg0, arg1);			
			return;
		}
		
		if (path.endsWith(LOGOUT_PATH) ) {
			request.setAttribute(LOGINOUT, "true");
			arg2.doFilter(arg0, arg1);			
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
			if (sessionId.equals("")) {
				LOGGER.warn("Session id is empty");
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
			CustomerSession cs = getCustomerSession(sessionId);
            if (cs != null) { 
            	request.setAttribute(LOGIN_USER, cs.getCustomerid());
            	arg2.doFilter(request, arg1);
            	LOGGER.info("Customer {} validated with session id {}", cs.getCustomerid(), sessionId);
				return;
			}
			else {
				LOGGER.warn("No customer session found with session id {}", sessionId);
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
		}

		LOGGER.warn("No session cookie provided");
		// if we got here, we didn't detect the session cookie, so we need to return 404
		response.sendError(HttpServletResponse.SC_FORBIDDEN);
		
	}
    
	private CustomerSession getCustomerSession(String sessionId) {
		return authenticationService.validateCustomerSession(sessionId);
	}
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
}
