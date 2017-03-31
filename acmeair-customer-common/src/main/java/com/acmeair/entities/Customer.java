/*******************************************************************************
* Copyright (c) 2013 IBM Corp.
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
package com.acmeair.entities;



public interface Customer {

	enum MemberShipStatus { NONE, SILVER, GOLD, PLATINUM, EXEC_PLATINUM, GRAPHITE };
	enum PhoneType { UNKNOWN, HOME, BUSINESS, MOBILE };
	
	
	String getCustomerId();
	
	String getUsername();
	
	void setUsername(String username);
	
	String getPassword();
	
	void setPassword(String password);
	
	MemberShipStatus getStatus();
	
	void setStatus(MemberShipStatus status);
	
	int getTotal_miles();
	
	int getMiles_ytd();
	
	String getPhoneNumber();

	void setPhoneNumber(String phoneNumber);

	PhoneType getPhoneNumberType();

	void setPhoneNumberType(PhoneType phoneNumberType);

	CustomerAddress getAddress();

	void setAddress(CustomerAddress address);

}
