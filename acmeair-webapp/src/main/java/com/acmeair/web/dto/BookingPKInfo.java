package com.acmeair.web.dto;

import javax.xml.bind.annotation.XmlElement;
import java.util.Objects;


public class BookingPKInfo {

	@XmlElement(name="id")
	private String id;
	
	@XmlElement(name="customerId")
	private String customerId;
	
	public BookingPKInfo() {
		
	}


	public BookingPKInfo(String customerId,String id) {
		
		this.id = id;
		this.customerId = customerId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BookingPKInfo that = (BookingPKInfo) o;
		return Objects.equals(id, that.id) &&
				Objects.equals(customerId, that.customerId);
	}

	@Override
	public String toString() {
		return "BookingPKInfo{" +
				"id='" + id + '\'' +
				", customerId='" + customerId + '\'' +
				'}';
	}
}
