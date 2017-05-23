package com.acmeair.config;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.acmeair.service.BookingService;
import com.acmeair.service.FlightService;
import io.servicecomb.provider.rest.common.RestSchema;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;

@RestSchema(schemaId = "config")
@Path("/info/config")
@Api(value = "Booking Service Configuration Information Service", produces = MediaType.APPLICATION_JSON)
public class AcmeAirConfiguration {

    Logger logger = Logger.getLogger(AcmeAirConfiguration.class.getName());

    @Autowired
    private BookingService bs;

    @Autowired
    private FlightService flightService;

    public AcmeAirConfiguration() {
        super();
    }

    @GET
    @Path("/dataServices")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<ServiceData> getDataServiceInfo() {
        // We don't use the ServiceLocator to lookup the DataService
        ArrayList<ServiceData> list = new ArrayList<>();
        return list;
    }

    @GET
    @Path("/activeDataService")
    @Produces(MediaType.APPLICATION_JSON)
    public String getActiveDataServiceInfo() {
        logger.fine("Get active Data Service info");
        // We just have one implementation here
        return "Morphia";
    }

    @GET
    @Path("/runtime")
    @Produces("application/json")
    public ArrayList<ServiceData> getRuntimeInfo() {
        try {
            logger.fine("Getting Runtime info");
            ArrayList<ServiceData> list = new ArrayList<ServiceData>();
            ServiceData data = new ServiceData();
            data.name = "Runtime";
            data.description = "Java";
            list.add(data);

            data = new ServiceData();
            data.name = "Version";
            data.description = System.getProperty("java.version");
            list.add(data);

            data = new ServiceData();
            data.name = "Vendor";
            data.description = System.getProperty("java.vendor");
            list.add(data);

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    class ServiceData {
        public String name = "";

        public String description = "";
    }

    @GET
    @Path("/countBookings")
    @Produces("application/json")
    public Long countBookings() {
        try {
            Long count = bs.count();
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return -1L;
        }
    }

    @GET
    @Path("/countFlights")
    @Produces("application/json")
    public Long countFlights() {
        try {
            Long count = flightService.countFlights();
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return -1L;
        }
    }

    @GET
    @Path("/countFlightSegments")
    @Produces("application/json")
    public Long countFlightSegments() {
        try {
            Long count = flightService.countFlightSegments();
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return -1L;
        }
    }

    @GET
    @Path("/countAirports")
    @Produces("application/json")
    public Long countAirports() {
        try {
            Long count = flightService.countAirports();
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return -1L;
        }
    }

}
