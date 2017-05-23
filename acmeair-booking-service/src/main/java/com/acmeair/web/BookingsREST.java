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

import java.util.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.acmeair.entities.Booking;
import com.acmeair.service.BookingService;
import com.acmeair.web.dto.BookingInfo;
import com.acmeair.web.dto.BookingReceiptInfo;
import io.servicecomb.provider.rest.common.RestSchema;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;

@RestSchema(schemaId = "bookings")
@Path("/api/bookings")
@Api(value = "Booking Service ", produces = MediaType.APPLICATION_JSON)
public class BookingsREST {

    @Autowired
    private BookingService bs;

    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Path("/bookflights")
    @Produces(MediaType.APPLICATION_JSON)
    public BookingReceiptInfo bookFlights(
            @FormParam("userid") String userid,
            @FormParam("toFlightId") String toFlightId,
            @FormParam("toFlightSegId") String toFlightSegId,
            @FormParam("retFlightId") String retFlightId,
            @FormParam("retFlightSegId") String retFlightSegId,
            @FormParam("oneWayFlight") boolean oneWay) {
        String bookingIdTo = bs.bookFlight(userid, toFlightSegId, toFlightId);
        String bookingIdReturn = null;
        if (!oneWay) {
            bookingIdReturn = bs.bookFlight(userid, retFlightSegId, retFlightId);
        }
        // YL. BookingInfo will only contains the booking generated keys as customer info is always available from the session
        BookingReceiptInfo bi;
        if (!oneWay)
            bi = new BookingReceiptInfo(bookingIdTo, bookingIdReturn, oneWay);
        else
            bi = new BookingReceiptInfo(bookingIdTo, null, oneWay);

        return bi;
    }

    @GET
    @Path("/bybookingnumber/{userid}/{number}")
    @Produces(MediaType.APPLICATION_JSON)
    public BookingInfo getBookingByNumber(
            @PathParam("number") String number,
            @PathParam("userid") String userid) {
        try {
            Booking b = bs.getBooking(userid, number);
            BookingInfo bi = null;
            if (b != null) {
                bi = new BookingInfo(b);
            }
            return bi;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GET
    @Path("/byuser/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BookingInfo> getBookingsByUser(@PathParam("user") String user) {
        try {
            List<Booking> list = bs.getBookingsByUser(user);
            List<BookingInfo> newList = new ArrayList<BookingInfo>();
            for (Booking b : list) {
                newList.add(new BookingInfo(b));
            }
            return newList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @POST
    @Consumes({"application/x-www-form-urlencoded"})
    @Path("/cancelbooking")
    @Produces(MediaType.APPLICATION_JSON)
    public String cancelBookingsByNumber(
            @FormParam("number") String number,
            @FormParam("userid") String userid) {
        bs.cancelBooking(userid, number);
        return "booking " + number + " deleted.";

    }

}
