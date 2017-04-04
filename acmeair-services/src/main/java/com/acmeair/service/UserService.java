package com.acmeair.service;

import com.acmeair.entities.CustomerSession;
import com.acmeair.web.dto.CustomerInfo;

public interface UserService {
    CustomerInfo getCustomerInfo(String customerId);
    CustomerSession validateCustomerSession(String sessionId);
}
