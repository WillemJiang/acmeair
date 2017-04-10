package com.acmeair;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(
        classes = AcmeAirApplication.class,
        webEnvironment = DEFINED_PORT)
@ActiveProfiles("jpa")
public class AcmeAirApplicationJpaTest extends AcmeAirApplicationTestBase {
}
