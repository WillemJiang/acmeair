package com.acmeair.web.dto;

import com.acmeair.entities.FlightSegment;

import java.util.Objects;

public class FlightSegmentInfo {

	private String _id;
	private String originPort;
	private String destPort;
	private int miles;
	
	public FlightSegmentInfo() {
		
	}
	public FlightSegmentInfo(FlightSegment flightSegment) {
		this._id = flightSegment.getFlightName();
		this.originPort = flightSegment.getOriginPort();
		this.destPort = flightSegment.getDestPort();
		this.miles = flightSegment.getMiles();
	}
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getOriginPort() {
		return originPort;
	}
	public void setOriginPort(String originPort) {
		this.originPort = originPort;
	}
	public String getDestPort() {
		return destPort;
	}
	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}
	public int getMiles() {
		return miles;
	}
	public void setMiles(int miles) {
		this.miles = miles;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FlightSegmentInfo that = (FlightSegmentInfo) o;
		return miles == that.miles &&
				Objects.equals(_id, that._id) &&
				Objects.equals(originPort, that.originPort) &&
				Objects.equals(destPort, that.destPort);
	}

	@Override
	public String toString() {
		return "FlightSegmentInfo{" +
				"_id='" + _id + '\'' +
				", originPort='" + originPort + '\'' +
				", destPort='" + destPort + '\'' +
				", miles=" + miles +
				'}';
	}
}
