package com.acmeair;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(
        classes = AcmeAirApplication.class,
        webEnvironment = DEFINED_PORT,
        properties = {
                "server.port=8092",
                "zuul.routes.bookings.url=http://localhost:8092",
                "spring.data.mongodb.host=localhost",
                "spring.data.mongodb.port=27017"
        })
@ActiveProfiles({"mongodb", "test"})
public class AcmeAirApplicationMongodbTest extends AcmeAirApplicationTestBase {
}
