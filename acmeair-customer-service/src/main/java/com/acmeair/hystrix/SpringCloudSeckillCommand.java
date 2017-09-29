/*
 *   Copyright 2017 Huawei Technologies Co., Ltd
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.acmeair.hystrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Profile("SpringCloud")
public class SpringCloudSeckillCommand extends SeckillCommand {
  private static final Logger logger = LoggerFactory.getLogger(SpringCloudSeckillCommand.class);

  @Autowired
  private LoadBalancerClient loadBalancer;

  SpringCloudSeckillCommand() {
    super(new RestTemplate());
  }

  protected String getSeckillServiceAddress() {
    String address = loadBalancer.choose(seckillServiceName).getUri().toString();
    logger.info("Just get the address {} from LoadBalancer.", address);
    return address + "/rest";
  }
}
