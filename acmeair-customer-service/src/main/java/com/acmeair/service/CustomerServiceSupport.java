/*******************************************************************************
* Copyright (c) 2013-2015 IBM Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*******************************************************************************/
package com.acmeair.service;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import com.acmeair.entities.Customer;
import com.acmeair.entities.CustomerSession;

public abstract class CustomerServiceSupport implements CustomerService {
	protected static final int DAYS_TO_ALLOW_SESSION = 1;
	
	@Inject
	protected KeyGenerator keyGenerator;
	
	
	protected abstract Customer getCustomer(String username);
	
	@Override
	public Customer getCustomerByUsername(String username) {
		Customer c = getCustomer(username);
		if (c != null) {
			c.setPassword(null);
		}
		return c;
	}

	@Override
	public boolean validateCustomer(String username, String password) {
		boolean validatedCustomer = false;
		Customer customerToValidate = getCustomer(username);
		if (customerToValidate != null) {
			validatedCustomer = password.equals(customerToValidate.getPassword());
		}
		return validatedCustomer;
	}
	
	@Override
	public Customer getCustomerByUsernameAndPassword(String username, String password) {
		Customer c = getCustomer(username);
		if (c != null && !c.getPassword().equals(password)) {
			return null;
		}
		// Should we also set the password to null?
		return c;
	}
		
	@Override
	public CustomerSession validateSession(String sessionid) {
		CustomerSession cSession = getSession(sessionid);
		if (cSession == null) {
			return null;
		}

		Date now = new Date();

		if (cSession.getTimeoutTime().before(now)) {
			removeSession(cSession);
			return null;
		}
		return cSession;		
	}
	
	protected abstract CustomerSession getSession(String sessionid);
	
	protected abstract void removeSession(CustomerSession session);
	
	@Override
	public CustomerSession createSession(String customerId) {
		String sessionId = keyGenerator.generate().toString();
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.add(Calendar.DAY_OF_YEAR, DAYS_TO_ALLOW_SESSION);
		Date expiration = c.getTime();
		
		return createSession(sessionId, customerId, now, expiration);
	}
	
	protected abstract CustomerSession createSession(String sessionId, String customerId, Date creation, Date expiration);
	
}
