package com.acmeair;

import org.junit.Ignore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles({"mongodb", "test"})
@SpringBootTest(
        classes = CustomerServiceApplication.class,
        webEnvironment = RANDOM_PORT,
        properties = {
                "spring.data.mongodb.host=localhost",
                "spring.data.mongodb.port=27017"
        })
@Ignore("Ignore the mongoDB test which could not run it")
public class CustomerServiceApplicationMongodbTest extends CustomerServiceApplicationTestBase {
}
