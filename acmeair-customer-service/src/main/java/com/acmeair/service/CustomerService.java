package com.acmeair.service;

import com.acmeair.entities.Customer;
import com.acmeair.entities.CustomerSession;
import com.acmeair.morphia.entities.CustomerAddressImpl;

public interface CustomerService {
    Customer createCustomer(String username, String password, Customer.MemberShipStatus status, int total_miles, int miles_ytd, String phoneNumber,
                                   Customer.PhoneType phoneNumberType, CustomerAddressImpl address
    );
    
    CustomerAddressImpl createAddress(String streetAddress1, String streetAddress2, String city, String stateProvince, String country, String postalCode);
    
    Customer updateCustomer(Customer customer);
    
    Customer getCustomerByUsername(String username);
    
    boolean validateCustomer(String username, String password);
    
    Customer getCustomerByUsernameAndPassword(String username, String password);
    
    CustomerSession validateSession(String sessionid);
    
    CustomerSession createSession(String customerId);
    
    void invalidateSession(String sessionid);
    
    Long count();
    
    Long countSessions();
}
