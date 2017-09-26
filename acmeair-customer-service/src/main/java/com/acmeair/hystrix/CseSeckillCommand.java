package com.acmeair.hystrix;

import io.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

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
