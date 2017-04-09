/*******************************************************************************
* Copyright (c) 2015 IBM Corp.
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
package com.acmeair.morphia.services;

import com.acmeair.entities.AirportCodeMapping;
import com.acmeair.entities.Flight;
import com.acmeair.entities.FlightSegment;
import com.acmeair.morphia.MorphiaConstants;
import com.acmeair.morphia.entities.AirportCodeMappingImpl;
import com.acmeair.morphia.entities.FlightImpl;
import com.acmeair.morphia.entities.FlightSegmentImpl;
import com.acmeair.morphia.repositories.AirportRepository;
import com.acmeair.morphia.repositories.FlightRepository;
import com.acmeair.morphia.repositories.FlightSegmentRepository;
import com.acmeair.service.DataService;
import com.acmeair.service.FlightService;
import com.acmeair.service.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@DataService(name=MorphiaConstants.KEY,description=MorphiaConstants.KEY_DESCRIPTION)
public class FlightServiceImpl extends FlightService implements  MorphiaConstants {

	//private final static Logger logger = Logger.getLogger(FlightService.class.getName()); 

	@Autowired
	private FlightRepository flightRepository;

	@Autowired
	private FlightSegmentRepository flightSegmentRepository;

	@Autowired
	private AirportRepository airportRepository;

	@Inject
	KeyGenerator keyGenerator;


	@Override
	public Long countFlights() {
		return flightRepository.count();
	}
	
	@Override
	public Long countFlightSegments() {
		return flightSegmentRepository.count();
	}
	
	@Override
	public Long countAirports() {
		return airportRepository.count();
	}

	protected Flight getFlight(String flightId, String segmentId) {
		return flightRepository.findOne(flightId);
	}

	@Override
	protected  FlightSegment getFlightSegment(String fromAirport, String toAirport){
		FlightSegment segment = flightSegmentRepository.findByOriginPortAndDestPort(fromAirport, toAirport);
		if (segment == null) {
			segment = new FlightSegmentImpl(); // put a sentinel value of a non-populated flightsegment 
		}
		return segment;
	}
	
	@Override
	protected  List<Flight> getFlightBySegment(FlightSegment segment, Date deptDate){
		List<FlightImpl> flightImpls;
		if(deptDate != null) {
			flightImpls = flightRepository.findByFlightSegmentIdAndScheduledDepartureTime(segment.getFlightName(), deptDate);
		} else {
			flightImpls = flightRepository.findByFlightSegmentId(segment.getFlightName());
		}
		List<Flight> flights = new ArrayList<>();
		if (flightImpls != null) {
			for (Flight flight : flightImpls) {
				flight.setFlightSegment(segment);
				flights.add(flight);
			}
		}
		return flights;
	}
	

	@Override
	public void storeAirportMapping(AirportCodeMapping mapping) {
		try{
			airportRepository.save((AirportCodeMappingImpl) mapping);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override 
	public AirportCodeMapping createAirportCodeMapping(String airportCode, String airportName){
		AirportCodeMapping acm = new AirportCodeMappingImpl(airportCode, airportName);
		return acm;
	}
	
	@Override
	public Flight createNewFlight(String flightSegmentId,
			Date scheduledDepartureTime, Date scheduledArrivalTime,
			BigDecimal firstClassBaseCost, BigDecimal economyClassBaseCost,
			int numFirstClassSeats, int numEconomyClassSeats,
			String airplaneTypeId) {
		String id = keyGenerator.generate().toString();
		Flight flight = new FlightImpl(id, flightSegmentId,
			scheduledDepartureTime, scheduledArrivalTime,
			firstClassBaseCost, economyClassBaseCost,
			numFirstClassSeats, numEconomyClassSeats,
			airplaneTypeId);
		try{
			flightRepository.save((FlightImpl) flight);
			return flight;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void storeFlightSegment(FlightSegment flightSeg) {
		try{
			flightSegmentRepository.save((FlightSegmentImpl) flightSeg);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override 
	public void storeFlightSegment(String flightName, String origPort, String destPort, int miles) {
		FlightSegment flightSeg = new FlightSegmentImpl(flightName, origPort, destPort, miles);
		storeFlightSegment(flightSeg);
	}
}
