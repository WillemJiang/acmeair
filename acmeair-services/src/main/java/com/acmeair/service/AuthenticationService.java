package com.acmeair.service;

import com.acmeair.entities.CustomerSession;

public interface AuthenticationService {
    CustomerSession validateCustomerSession(String sessionId);
}
