package com.acmeair;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(
        classes = AcmeAirApplication.class,
        webEnvironment = RANDOM_PORT)
@ActiveProfiles({"jpa", "test"})
public class AcmeAirApplicationJpaTest extends AcmeAirApplicationTestBase {
}
