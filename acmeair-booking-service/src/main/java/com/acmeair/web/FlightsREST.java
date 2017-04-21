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
package com.acmeair.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.acmeair.entities.Flight;
import com.acmeair.entities.FlightSegment;
import com.acmeair.service.FlightService;
import com.acmeair.web.dto.TripFlightOptions;
import com.acmeair.web.dto.TripLegInfo;
import com.huawei.paas.cse.provider.rest.common.RestSchema;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;

@RestSchema(schemaId = "flights")
@Path("/api/flights")
@Api(value = "Flight Service ", produces = MediaType.APPLICATION_JSON)
public class FlightsREST {
    @Autowired
    private FlightService flightService;

    // TODO:  Consider a pure GET implementation of this service, but maybe not much value due to infrequent similar searches
    @POST
    @Path("/queryflights")
    @Consumes({"application/x-www-form-urlencoded"})
    @Produces("application/json")
    public TripFlightOptions getTripFlights(
            @FormParam("fromAirport") String fromAirport,
            @FormParam("toAirport") String toAirport,
            @FormParam("fromDate") String fromDate,
            @FormParam("returnDate") String returnDate,
            @FormParam("oneWay") boolean oneWay) {
        TripFlightOptions options = new TripFlightOptions();
        ArrayList<TripLegInfo> legs = new ArrayList<TripLegInfo>();

        TripLegInfo toInfo = new TripLegInfo();
        List<Flight> toFlights =
            flightService.getFlightByAirportsAndDepartureDate(fromAirport, toAirport, new Date(fromDate));
        toInfo.addFlightsOptions(toFlights);
        legs.add(toInfo);
        toInfo.setCurrentPage(0);
        toInfo.setHasMoreOptions(false);
        toInfo.setNumPages(1);
        toInfo.setPageSize(TripLegInfo.DEFAULT_PAGE_SIZE);

        if (!oneWay) {
            TripLegInfo retInfo = new TripLegInfo();
            List<Flight> retFlights =
                flightService.getFlightByAirportsAndDepartureDate(toAirport, fromAirport, new Date(returnDate));
            retInfo.addFlightsOptions(retFlights);
            legs.add(retInfo);
            retInfo.setCurrentPage(0);
            retInfo.setHasMoreOptions(false);
            retInfo.setNumPages(1);
            retInfo.setPageSize(TripLegInfo.DEFAULT_PAGE_SIZE);
            options.setTripLegs(2);
        } else {
            options.setTripLegs(1);
        }

        options.setTripFlights(legs);

        return options;
    }

    @POST
    @Path("/browseflights")
    @Consumes({"application/x-www-form-urlencoded"})
    @Produces("application/json")
    public TripFlightOptions browseFlights(
            @FormParam("fromAirport") String fromAirport,
            @FormParam("toAirport") String toAirport,
            @FormParam("oneWay") boolean oneWay) {
        TripFlightOptions options = new TripFlightOptions();
        ArrayList<TripLegInfo> legs = new ArrayList<TripLegInfo>();

        TripLegInfo toInfo = new TripLegInfo();
        List<Flight> toFlights = flightService.getFlightByAirports(fromAirport, toAirport);
        toInfo.addFlightsOptions(toFlights);
        legs.add(toInfo);
        toInfo.setCurrentPage(0);
        toInfo.setHasMoreOptions(false);
        toInfo.setNumPages(1);
        toInfo.setPageSize(TripLegInfo.DEFAULT_PAGE_SIZE);

        if (!oneWay) {
            TripLegInfo retInfo = new TripLegInfo();
            List<Flight> retFlights = flightService.getFlightByAirports(toAirport, fromAirport);
            retInfo.addFlightsOptions(retFlights);
            legs.add(retInfo);
            retInfo.setCurrentPage(0);
            retInfo.setHasMoreOptions(false);
            retInfo.setNumPages(1);
            retInfo.setPageSize(TripLegInfo.DEFAULT_PAGE_SIZE);
            options.setTripLegs(2);
        } else {
            options.setTripLegs(1);
        }

        options.setTripFlights(legs);

        return options;
    }

    @POST
    @Path("/createsegment")
    @Consumes({"application/x-www-form-urlencoded"})
    @Produces("application/json")
    public FlightSegment createFlightSegment(@FormParam("flightName") String flightName,
            @FormParam("origPort") String origPort, @FormParam("destPort") String destPort,
            @FormParam("miles") int miles) {
        return flightService.storeFlightSegment(flightName, origPort, destPort, miles);
    }

    @POST
    @Path("/createflight")
    @Consumes({"application/x-www-form-urlencoded"})
    @Produces("application/json")
    public Flight createFlight(@FormParam("flightSegmentId") String flightSegmentId,
            @FormParam("scheduledDepartureTime") String scheduledDepartureTime,
            @FormParam("scheduledArrivalTime") String scheduledArrivalTime,
            @FormParam("firstClassBaseCost") BigDecimal firstClassBaseCost,
            @FormParam("economyClassBaseCost") BigDecimal economyClassBaseCost,
            @FormParam("numFirstClassSeats") int numFirstClassSeats,
            @FormParam("numEconomyClassSeats") int numEconomyClassSeats,
            @FormParam("airplaneTypeId") String airplaneTypeId) {
        return flightService.createNewFlight(flightSegmentId,
                new Date(scheduledDepartureTime),
                new Date(scheduledArrivalTime),
                firstClassBaseCost,
                economyClassBaseCost,
                numFirstClassSeats,
                numEconomyClassSeats,
                airplaneTypeId);
    }

}
