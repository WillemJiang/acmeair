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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.netflix.zuul.context.RequestContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class LoginCookieUpdateFilterTest {

  private LoginCookieUpdateFilter filter = new LoginCookieUpdateFilter();
  private RequestContext context = RequestContext.getCurrentContext();
  private HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
  private HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

  @Before
  public void setUp() throws Exception {
    context.setRequest(request);
    context.setResponse(response);
  }

  @After
  public void tearDown() throws Exception {
    RequestContext.getCurrentContext().clear();
  }

  @Test
  public void skippedPathNotEndsWithLogin() {
    context.setResponseStatusCode(200);
    when(response.getHeader(SET_COOKIE)).thenReturn(null);

    when(request.getRequestURI()).thenReturn("/login");
    assertThat(this.filter.shouldFilter(), is(false));

    when(request.getRequestURI()).thenReturn("/api/login/validate");
    assertThat(this.filter.shouldFilter(), is(false));

    when(request.getRequestURI()).thenReturn("/api/login/");
    assertThat(this.filter.shouldFilter(), is(false));
  }

  @Test
  public void skippedWhenStatusIsNot200() {
    when(response.getHeader(SET_COOKIE)).thenReturn(null);
    when(request.getRequestURI()).thenReturn("/api/login");

    when(response.getStatus()).thenReturn(404);
    assertThat(this.filter.shouldFilter(), is(false));
  }

  @Test
  public void skippedWhenCookieHeaderIsNotNull() {
    context.setResponseStatusCode(200);
    when(request.getRequestURI()).thenReturn("/api/login");

    when(response.getHeader(SET_COOKIE)).thenReturn("sessionid=abc");
    assertThat(this.filter.shouldFilter(), is(false));
  }

  @Test
  public void ensureCookieMovedFromByteStreamToHeader() throws IOException {
    context.setResponseStatusCode(200);
    when(request.getRequestURI()).thenReturn("/api/login");
    when(response.getHeader(SET_COOKIE)).thenReturn(null);
    assertThat(this.filter.shouldFilter(), is(true));

    context.setResponseDataStream(new ByteArrayInputStream("{\"sessionid\": \"abc-123\"}".getBytes()));
    this.filter.run();
    // there is no way to get the cookie in response,
    // we can only validate the data stream had been consumed.
    assertThat(context.getResponseDataStream().read(), is(-1));
  }
}