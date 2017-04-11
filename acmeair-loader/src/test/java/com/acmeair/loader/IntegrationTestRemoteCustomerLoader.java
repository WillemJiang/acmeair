package com.acmeair.loader;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Primary
@Component
@Profile("sit")
class IntegrationTestRemoteCustomerLoader extends RemoteCustomerLoader {

    @Override
    protected String getCustomerServiceAddress() {
        String serviceAddress = super.getCustomerServiceAddress();
        return "http://localhost" + serviceAddress.substring(serviceAddress.lastIndexOf(":"));
    }
}
