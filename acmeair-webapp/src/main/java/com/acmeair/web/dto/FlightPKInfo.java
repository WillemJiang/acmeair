package com.acmeair.web.dto;

import java.util.Objects;

public class FlightPKInfo {

	private String id;
	private String flightSegmentId;
	
	FlightPKInfo(){}
	FlightPKInfo(String flightSegmentId,String id){
		this.id = id;
		this.flightSegmentId = flightSegmentId;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFlightSegmentId() {
		return flightSegmentId;
	}
	public void setFlightSegmentId(String flightSegmentId) {
		this.flightSegmentId = flightSegmentId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FlightPKInfo that = (FlightPKInfo) o;
		return Objects.equals(id, that.id) &&
				Objects.equals(flightSegmentId, that.flightSegmentId);
	}

	@Override
	public String toString() {
		return "FlightPKInfo{" +
				"id='" + id + '\'' +
				", flightSegmentId='" + flightSegmentId + '\'' +
				'}';
	}
}
