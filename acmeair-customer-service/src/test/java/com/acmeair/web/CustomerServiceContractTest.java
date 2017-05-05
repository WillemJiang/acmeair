package com.acmeair.web;

import au.com.dius.pact.provider.junit.PactRunner;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.acmeair.entities.Customer;
import com.acmeair.entities.CustomerAddress;
import com.acmeair.morphia.entities.CustomerAddressImpl;
import com.acmeair.morphia.entities.CustomerImpl;
import com.acmeair.service.CustomerService;
import com.acmeair.web.dto.CustomerSessionInfo;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import test.com.acmeair.service.CustomerRestApplication;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@RunWith(PactRunner.class)
@PactFolder("../target/pacts")
@Provider("CustomerService")
public class CustomerServiceContractTest {
    private static CustomerService customerService;
    private static ConfigurableApplicationContext customerApplicationContext;

    @TestTarget
    public final Target target = new HttpTarget(8081);

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private final RuntimeException exception = new RuntimeException("oops");

    private final Customer            customer            = new CustomerImpl(
            "sean-123",
            "password",
            Customer.MemberShipStatus.PLATINUM,
            1000,
            356,
            new CustomerAddressImpl(
                    "White Plains",
                    "Avenue 1",
                    "New York City",
                    "New York",
                    "USA",
                    "100010"
            ),
            "100-100-111",
            Customer.PhoneType.MOBILE
    );

    private final CustomerSessionInfo customerSessionInfo = new CustomerSessionInfo(
            "session-id-434200",
            "sean-123",
            dateOf("2017-04-08"),
            dateOf("2027-04-07")
    );

    @BeforeClass
    public static void startCustomerService() {
        customerApplicationContext = SpringApplication.run(CustomerRestApplication.class, "--server.port=8081", "--spring.profiles.active=test,jpa");
        customerService = customerApplicationContext.getBean(CustomerService.class);

        FixtureFactoryLoader.loadTemplates("com.acmeair.customer.templates");
    }

    @AfterClass
    public static void closeCustomerService() {
        customerApplicationContext.close();
    }

    @Before
    public void setUp() {
        reset(customerService);
    }

    @State("Customer Sean is registered")
    public void foundSpecifiedCustomer() {
        when(customerService.getCustomerByUsername(customer.getUsername())).thenReturn(customer);
        when(customerService.validateSession(customerSessionInfo.getId())).thenReturn(customerSessionInfo);
    }

    @State("No customer Mike found")
    public void noSuchCustomerFound() {
        when(customerService.getCustomerByUsername("mike")).thenReturn(null);
        when(customerService.validateSession("session-mike-123")).thenReturn(null);
    }

    private Date dateOf(String source) {
        try {
            return dateFormat.parse(source);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Wrong date format. Expected is yyyy-MM-dd", e);
        }
    }
    @State("Remote customer loader is available")
    public void customerLoaderIsAvailable() {
        CustomerAddress address = Fixture.from(CustomerAddressImpl.class).gimme("valid");

        when(customerService.createAddress(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString()
        )).thenReturn(address);
    }

    @State("Remote customer loader is not available")
    public void customerLoaderIsNotAvailable() {
        when(customerService.createAddress(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString()
        )).thenThrow(exception);
    }
}
