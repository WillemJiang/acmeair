package com.acmeair.morphia.services;

import com.acmeair.entities.Customer;
import com.acmeair.entities.Customer.MemberShipStatus;
import com.acmeair.entities.Customer.PhoneType;
import com.acmeair.entities.CustomerAddress;
import com.acmeair.entities.CustomerSession;
import com.acmeair.morphia.entities.CustomerAddressImpl;
import com.acmeair.morphia.entities.CustomerImpl;
import com.acmeair.morphia.entities.CustomerSessionImpl;
import com.acmeair.morphia.repositories.CustomerRepository;
import com.acmeair.morphia.repositories.CustomerSessionRepository;
import com.acmeair.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
class CustomerServiceImpl extends CustomerService {
		
//	private final static Logger logger = Logger.getLogger(CustomerService.class.getName()); 

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CustomerSessionRepository sessionRepository;


	@Override
	public Long count() {
		return customerRepository.count();
	}
	
	@Override
	public Long countSessions() {
		return sessionRepository.count();
	}
	
	public Customer createCustomer(String username, String password,
			MemberShipStatus status, int total_miles, int miles_ytd,
			String phoneNumber, PhoneType phoneNumberType,
			CustomerAddressImpl address) {
	
		CustomerImpl customer = new CustomerImpl(username, password, status, total_miles, miles_ytd, address, phoneNumber, phoneNumberType);
		customerRepository.save(customer);
		return customer;
	}
	
	public CustomerAddressImpl createAddress (String streetAddress1, String streetAddress2,
			String city, String stateProvince, String country, String postalCode){
		return new CustomerAddressImpl(streetAddress1, streetAddress2,
				 city, stateProvince,  country,  postalCode);
	}

	@Override
	public Customer updateCustomer(Customer customer) {
		customerRepository.save((CustomerImpl) customer);
		return customer;
	}

	@Override
	protected Customer getCustomer(String username) {
		return customerRepository.findOne(username);
	}

	@Override
	protected CustomerSession getSession(String sessionid){
		return sessionRepository.findOne(sessionid);
	}
	
	@Override
	protected void removeSession(CustomerSession session){		
		sessionRepository.delete(session.getId());
	}
	
	@Override
	protected  CustomerSession createSession(String sessionId, String customerId, Date creation, Date expiration) {
		CustomerSessionImpl cSession = new CustomerSessionImpl(sessionId, customerId, creation, expiration);
		sessionRepository.save(cSession);
		return cSession;
	}

	@Override
	public void invalidateSession(String sessionid) {		
		sessionRepository.delete(sessionid);
	}

}
