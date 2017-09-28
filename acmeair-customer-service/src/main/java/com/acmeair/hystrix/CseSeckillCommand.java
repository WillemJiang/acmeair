package com.acmeair.hystrix;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import io.servicecomb.provider.springmvc.reference.RestTemplateBuilder;

@Service
@Profile("!SpringCloud")
public class CseSeckillCommand extends SeckillCommand {

  CseSeckillCommand() {
    super(RestTemplateBuilder.create());
  }

  @Override
  protected String getSeckillServiceAddress() {
    return "cse://" + seckillServiceName;
  }
}
