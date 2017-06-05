package com.acmeair.web;

import com.acmeair.entities.TokenInfo;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.NewCookie;
import java.io.IOException;

import static com.acmeair.entities.CustomerSession.SESSIONID_COOKIE_NAME;
import static javax.ws.rs.core.NewCookie.DEFAULT_MAX_AGE;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

class LoginResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        if (doesPathContain(requestContext, "/api/login")) {
            String sessionId = ((TokenInfo) responseContext.getEntity()).getSessionid();
            addCookie(responseContext, newCookie(sessionId));
            responseContext.setEntity("logged in");
        } else if (doesPathContain(requestContext, "/api/login/logout")) {
            addCookie(responseContext, newCookie(""));
        }
    }

    private boolean doesPathContain(ContainerRequestContext requestContext, String suffix) {
        return requestContext.getUriInfo().getAbsolutePath().getPath().endsWith(suffix);
    }

    private void addCookie(ContainerResponseContext responseContext, NewCookie cookie) {
        responseContext.getHeaders().add(SET_COOKIE, cookie);
    }

    private NewCookie newCookie(String value) {
        return new NewCookie(SESSIONID_COOKIE_NAME, value, "/", null, null, DEFAULT_MAX_AGE, false);
    }
}
