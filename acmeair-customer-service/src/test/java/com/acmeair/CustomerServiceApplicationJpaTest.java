package com.acmeair;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("jpa")
@SpringBootTest(
        classes = CustomerServiceApplication.class,
        webEnvironment = RANDOM_PORT)
public class CustomerServiceApplicationJpaTest extends CustomerServiceApplicationTestBase {
}
