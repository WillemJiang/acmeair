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

import com.netflix.zuul.context.RequestContext;
import org.junit.After;
import org.junit.Test;

public class UriRewriteFilterTest {

  private UriRewriteFilter filter = new UriRewriteFilter();
  private RequestContext context = RequestContext.getCurrentContext();

  @After
  public void tearDown() throws Exception {
    RequestContext.getCurrentContext().clear();
  }

  @Test
  public void skippedIfRestNotInRequestURI() {
    context.set("requestURI", "/api/login");
    assertThat(this.filter.shouldFilter(), is(false));
  }

  @Test
  public void skippedIfRequestURINotStartsWithRest() {
    context.set("requestURI", "/abc/rest/api/login");
    assertThat(this.filter.shouldFilter(), is(false));
  }

  @Test
  public void ensureRestRemoveFromRequestURI() {
    context.set("requestURI", "/rest/api/login");
    assertThat(this.filter.shouldFilter(), is(true));

    this.filter.run();
    assertThat(context.get("requestURI"), is("/api/login"));
  }
}