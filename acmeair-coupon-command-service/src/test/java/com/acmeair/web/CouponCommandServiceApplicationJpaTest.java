package com.acmeair.web;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.acmeair.CouponCommandServiceApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    classes = CouponCommandServiceApplication.class,
    webEnvironment = RANDOM_PORT)
@ActiveProfiles({"jpa", "SpringCloud", "test"})
public class CouponCommandServiceApplicationJpaTest extends CouponCommandServiceApplicationTestBase {

}
