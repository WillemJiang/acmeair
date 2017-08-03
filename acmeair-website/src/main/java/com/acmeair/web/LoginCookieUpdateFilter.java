/*
 *  Copyright 2017 Huawei Technologies Co., Ltd
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.acmeair.web;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import io.servicecomb.common.rest.codec.RestObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoginCookieUpdateFilter extends ZuulFilter {

  private static final Logger logger = LoggerFactory.getLogger(LoginCookieUpdateFilter.class);
  private final int MAX_COOKIE_SIZE = 4096;

  @Override
  public String filterType() {
    return "post";
  }

  @Override
  public int filterOrder() {
    return 0;
  }

  @Override
  public boolean shouldFilter() {
    RequestContext ctx = RequestContext.getCurrentContext();
    return ctx.getRequest().getRequestURI().contains("/api/login") &&
        ctx.getResponseStatusCode() == 200 &&
        ctx.getResponse().getHeader(SET_COOKIE) == null;
  }

  @Override
  public Object run() {
    logger.info("Moving cookies from byte stream to header");
    RequestContext ctx = RequestContext.getCurrentContext();

    String rawCookies = responseBodyOf(ctx);
    Map<String, String> cookies = jsonOf(ctx, rawCookies);

    HttpServletResponse response = ctx.getResponse();
    for (Map.Entry<String, String> entry : cookies.entrySet()) {
      response.addCookie(new Cookie(entry.getKey(), entry.getValue()));
    }
    ctx.setResponse(response);

    return null;
  }

  private Map<String, String> jsonOf(RequestContext context, String content) {
    try {
      return RestObjectMapper.INSTANCE.readValue(content, HashMap.class);
    } catch (IOException e) {
      context.setResponseStatusCode(SC_INTERNAL_SERVER_ERROR);
      throw new IllegalStateException("Failed to parse json in response body", e);
    }
  }

  private String responseBodyOf(RequestContext context) {
    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(MAX_COOKIE_SIZE)) {
      IOUtils.copy(context.getResponseDataStream(), outputStream);
      return outputStream.toString();
    } catch (IOException e) {
      context.setResponseStatusCode(SC_INTERNAL_SERVER_ERROR);
      throw new IllegalStateException("Failed to read response body", e);
    }
  }
}
