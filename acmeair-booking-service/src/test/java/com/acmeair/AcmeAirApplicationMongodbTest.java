package com.acmeair;

import org.junit.Ignore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(
        classes = AcmeAirApplication.class,
        webEnvironment = RANDOM_PORT,
        properties = {
                "flights.max.per.segment=3",
                "server.port=8092",
                "zuul.routes.bookings.url=http://localhost:8092",
                "spring.data.mongodb.host=localhost",
                "spring.data.mongodb.port=27017"
        })
@ActiveProfiles({"mongodb", "SpringCloud", "test", "perf"})
@Ignore("Ignore the mongoDB test which could not run it")
public class AcmeAirApplicationMongodbTest extends AcmeAirApplicationTestBase {
}
