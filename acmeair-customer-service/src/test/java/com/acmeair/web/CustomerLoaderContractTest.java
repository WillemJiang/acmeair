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
import com.acmeair.entities.CustomerAddress;
import com.acmeair.morphia.entities.CustomerAddressImpl;
import com.acmeair.service.CustomerService;
import test.com.acmeair.CustomerLoaderApplication;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@RunWith(PactRunner.class)
@PactFolder("../acmeair-loader/target/pacts")
@Provider("CustomerService")
@ActiveProfiles("test")
public class CustomerLoaderContractTest {
    private static CustomerService customerService;
    private static ConfigurableApplicationContext customerApplicationContext;

    @TestTarget
    public final Target target = new HttpTarget(8081);

    private final RuntimeException exception = new RuntimeException("oops");

    @BeforeClass
    public static void startBookService() {
        customerApplicationContext = SpringApplication.run(CustomerLoaderApplication.class, "--server.port=8081");
        customerService = customerApplicationContext.getBean(CustomerService.class);

        FixtureFactoryLoader.loadTemplates("com.acmeair.customer.templates");
    }

    @AfterClass
    public static void closeBookService() {
        customerApplicationContext.close();
    }

    @Before
    public void setUp() {
        reset(customerService);
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
