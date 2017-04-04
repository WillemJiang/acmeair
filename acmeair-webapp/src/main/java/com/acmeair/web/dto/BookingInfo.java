package com.acmeair.web.dto;

import java.util.Date;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.acmeair.entities.Booking;

@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlRootElement(name="Booking")
public class BookingInfo {

	@XmlElement(name="bookingId")
	private String bookingId;	
	
	@XmlElement(name="flightId")
	private String flightId;
	
	@XmlElement(name="customerId")
	private String customerId;
	
	@XmlElement(name="dateOfBooking")
	private Date dateOfBooking;
	
	@XmlElement(name="pkey")
	private BookingPKInfo pkey;
	
	public BookingInfo() {
		
	}

	public BookingInfo(Booking booking){
		this.bookingId = booking.getBookingId();
		this.flightId = booking.getFlightId();
		this.customerId = booking.getCustomerId();
		this.dateOfBooking = booking.getDateOfBooking();
		this.pkey = new BookingPKInfo(this.customerId, this.bookingId);
	}
	
	
	public String getBookingId() {
		return bookingId;
	}
	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}
	public String getFlightId() {
		return flightId;
	}
	public void setFlightId(String flightId) {
		this.flightId = flightId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public Date getDateOfBooking() {
		return dateOfBooking;
	}
	public void setDateOfBooking(Date dateOfBooking) {
		this.dateOfBooking = dateOfBooking;
	}	
	public BookingPKInfo getPkey(){
		return pkey;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BookingInfo that = (BookingInfo) o;
		return Objects.equals(bookingId, that.bookingId) &&
				Objects.equals(flightId, that.flightId) &&
				Objects.equals(customerId, that.customerId) &&
				Objects.equals(dateOfBooking, that.dateOfBooking) &&
				Objects.equals(pkey, that.pkey);
	}

	@Override
	public String toString() {
		return "BookingInfo{" +
				"bookingId='" + bookingId + '\'' +
				", flightId='" + flightId + '\'' +
				", customerId='" + customerId + '\'' +
				", dateOfBooking=" + dateOfBooking +
				", pkey=" + pkey +
				'}';
	}
}
