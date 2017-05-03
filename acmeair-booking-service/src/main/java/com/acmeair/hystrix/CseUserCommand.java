package com.acmeair.hystrix;

import com.huawei.paas.cse.provider.springmvc.reference.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("cse")
public class CseUserCommand extends UserCommand {

    CseUserCommand() {
        super(RestTemplateBuilder.create());
    }

    @Override
    protected String getCustomerServiceAddress() {
        return "cse://" + customerServiceName;
    }
}
