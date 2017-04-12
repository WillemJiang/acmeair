package com.acmeair.service;

import com.acmeair.web.dto.CustomerInfo;

public interface UserService {
    CustomerInfo getCustomerInfo(String customerId);
}
