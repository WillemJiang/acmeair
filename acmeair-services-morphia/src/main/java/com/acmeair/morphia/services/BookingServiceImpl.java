package com.acmeair.morphia.services;

import com.acmeair.entities.Booking;
import com.acmeair.entities.Flight;
import com.acmeair.morphia.entities.BookingImpl;
import com.acmeair.morphia.repositories.BookingRepository;
import com.acmeair.service.BookingService;
import com.acmeair.service.FlightService;
import com.acmeair.service.KeyGenerator;
import com.acmeair.service.UserService;
import com.acmeair.web.dto.CustomerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    //private final static Logger logger = Logger.getLogger(BookingService.class.getName()); 

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    KeyGenerator keyGenerator;

    @Autowired
    private FlightService flightService;

    @Autowired
    private UserService userService;

    public String bookFlight(String customerId, String flightId) {
        try {
            Flight f = flightService.getFlightByFlightId(flightId, null);

            CustomerInfo customerInfo = userService.getCustomerInfo(customerId);

            BookingImpl newBooking = new BookingImpl(keyGenerator.generate().toString(), new Date(), customerInfo, f);

            bookingRepository.save(newBooking);
            return newBooking.getBookingId();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String bookFlight(String customerId, String flightSegmentId, String flightId) {
        return bookFlight(customerId, flightId);
    }

    @Override
    public Booking getBooking(String user, String bookingId) {
        try {
            return bookingRepository.findOne(bookingId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Booking> getBookingsByUser(String user) {
        try {
            List<BookingImpl> bookingImpls = bookingRepository.findByCustomerId(user);
            List<Booking> bookings = new ArrayList<Booking>();
            for (Booking b : bookingImpls) {
                bookings.add(b);
            }
            return bookings;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelBooking(String user, String bookingId) {
        try {
            bookingRepository.delete(bookingId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long count() {
        return bookingRepository.count();
    }
}
