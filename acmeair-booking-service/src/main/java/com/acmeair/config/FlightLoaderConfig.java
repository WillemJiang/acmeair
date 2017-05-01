package com.acmeair.config;

import com.acmeair.loader.FlightLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

@Profile("perf")
@Configuration
class FlightLoaderConfig {
    private static final Logger logger = LoggerFactory.getLogger(FlightLoaderConfig.class);
    
    @Autowired
    private FlightLoader flightLoader;

    @PostConstruct
    void populateFlights() {
        try {
            long start = System.currentTimeMillis();
            logger.info("Start loading flights");
            flightLoader.loadFlights();
            long stop = System.currentTimeMillis();
            double length = (stop - start) / 1000.0;
            logger.info("Finished loading flights in {} seconds", length);
        } catch (Exception e) {
            logger.error("Failed to fights", e);
        }
    }
}