package com.acmeair.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Profile("SpringCloud")
class SpringCloudCustomerLoader extends RemoteCustomerLoader {
    private static final Logger logger = LoggerFactory.getLogger(SpringCloudCustomerLoader.class);

    @Autowired
    private LoadBalancerClient loadBalancer;

    SpringCloudCustomerLoader() {
        super(new RestTemplate());
    }

    protected String getCustomerServiceAddress() {
        return customerServiceAddress() + "/rest";
    }

    protected String customerServiceAddress() {
        String address = loadBalancer.choose(customerServiceName).getUri().toString();
        logger.info("Just get the address {} from LoadBalancer.", address);
        return address;
    }
}
