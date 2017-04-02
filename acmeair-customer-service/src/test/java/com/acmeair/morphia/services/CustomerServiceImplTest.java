package com.acmeair.morphia.services;

import com.acmeair.entities.Customer;
import com.acmeair.morphia.entities.CustomerAddressImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mongodb.morphia.Datastore;

import static com.acmeair.entities.Customer.MemberShipStatus.NONE;
import static com.acmeair.entities.Customer.PhoneType.UNKNOWN;
import static io.seanyinx.github.unit.scaffolding.Randomness.nextInt;
import static io.seanyinx.github.unit.scaffolding.Randomness.uniquify;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceImplTest {

    private final String              username        = uniquify("username");
    private final String              password        = uniquify("password");
    private final int                 totalMiles      = nextInt();
    private final int                 milesYtd        = nextInt();
    private final String              phoneNumber     = uniquify("0");
    private final CustomerAddressImpl address         = new CustomerAddressImpl(
            uniquify("broad way"),
            "",
            "new york city",
            "NY",
            "USA",
            uniquify("0")
    );

    @InjectMocks
    private final CustomerServiceImpl customerService = new CustomerServiceImpl();

    @Mock
    private Datastore datastore;

    @Test
    public void savesCustomerIntoDatabase() {
        Customer customer = customerService.createCustomer(
                username,
                password,
                NONE,
                totalMiles,
                milesYtd,
                phoneNumber,
                UNKNOWN,
                address
        );

        assertThat(customer.getUsername(), is(username));
        assertThat(customer.getPassword(), is(password));
        assertThat(customer.getStatus(), is(NONE));
        assertThat(customer.getTotal_miles(), is(totalMiles));
        assertThat(customer.getMiles_ytd(), is(milesYtd));
        assertThat(customer.getPhoneNumber(), is(phoneNumber));
        assertThat(customer.getPhoneNumberType(), is(UNKNOWN));
        assertThat(customer.getAddress(), is(address));

        verify(datastore).save(customer);
    }
}
