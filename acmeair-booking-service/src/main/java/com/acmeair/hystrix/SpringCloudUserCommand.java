package com.acmeair.hystrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Profile("!cse")
class SpringCloudUserCommand extends UserCommand {
    private static final Logger logger = LoggerFactory.getLogger(SpringCloudUserCommand.class);

    @Autowired
    private LoadBalancerClient loadBalancer;

    SpringCloudUserCommand() {
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
