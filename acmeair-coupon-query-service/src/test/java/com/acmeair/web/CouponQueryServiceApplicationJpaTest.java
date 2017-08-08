package com.acmeair.web;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.acmeair.CouponQueryServiceApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    classes = CouponQueryServiceApplication.class,
    webEnvironment = RANDOM_PORT)
@ActiveProfiles({"jpa", "SpringCloud", "test"})
public class CouponQueryServiceApplicationJpaTest extends CouponQueryServiceApplicationTestBase {

}
