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

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!SpringCloud")
@Component
class UriRewriteFilter extends ZuulFilter {

  private static final Logger logger = LoggerFactory.getLogger(UriRewriteFilter.class);

  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public int filterOrder() {
    return Integer.MAX_VALUE;
  }

  @Override
  public boolean shouldFilter() {
    HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
    String uri = request.getRequestURI();
    return uri.startsWith("/rest/");
  }

  @Override
  public Object run() {
    RequestContext ctx = RequestContext.getCurrentContext();
    String uri = ctx.getRequest().getRequestURI();
    String newUri = uri.replaceFirst("^/rest", "");
    logger.info("Rewritten request uri from {} to {}", uri, newUri);
    ctx.put("requestURI", newUri);

    return null;
  }
}
