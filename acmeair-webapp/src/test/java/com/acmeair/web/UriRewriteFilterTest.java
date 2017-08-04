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
import static org.junit.Assert.*;

import com.netflix.zuul.context.RequestContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class UriRewriteFilterTest {

  private UriRewriteFilter filter = new UriRewriteFilter();
  private MockHttpServletRequest request = new MockHttpServletRequest();


  @Before
  public void setUp() throws Exception {
    RequestContext ctx = RequestContext.getCurrentContext();
    ctx.setRequest(request);
  }

  @After
  public void tearDown() throws Exception {
    RequestContext.getCurrentContext().clear();
  }

  @Test
  public void skippedIfRestNotInRequestURI() {
    request.setRequestURI("/api/login");
    assertThat(this.filter.shouldFilter(), is(false));
  }

  @Test
  public void skippedIfRequestURINotStartsWithRest() {
    request.setRequestURI("/abc/rest/api/login");
    assertThat(this.filter.shouldFilter(), is(false));
  }

  @Test
  public void ensureRestRemoveFromRequestURI() {
    request.setRequestURI("/rest/api/login");
    assertThat(this.filter.shouldFilter(), is(true));

    this.filter.run();
    assertThat(RequestContext.getCurrentContext().get("requestURI"), is("/api/login"));
  }
}