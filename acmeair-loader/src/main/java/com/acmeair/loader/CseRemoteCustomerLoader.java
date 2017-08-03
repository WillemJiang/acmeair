package com.acmeair.loader;

import io.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!SpringCloud")
class CseRemoteCustomerLoader extends RemoteCustomerLoader {
    CseRemoteCustomerLoader() {
        super(RestTemplateBuilder.create());
    }

    @Override
    protected String getCustomerServiceAddress() {
        return "cse://" + customerServiceName;
    }
}
