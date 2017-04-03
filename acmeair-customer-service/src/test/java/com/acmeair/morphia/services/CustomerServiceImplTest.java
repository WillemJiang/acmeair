package com.acmeair.morphia.services;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.acmeair.entities.Customer;
import com.acmeair.entities.CustomerAddress;
import com.acmeair.morphia.entities.CustomerAddressImpl;
import com.acmeair.morphia.entities.CustomerImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.FieldEnd;
import org.mongodb.morphia.query.Query;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceImplTest {
    private final Query           query    = Mockito.mock(Query.class);
    private final FieldEnd<Query> fieldEnd = Mockito.mock(FieldEnd.class);

    private CustomerAddress address;
    private Customer        customer;

    @InjectMocks
    private final CustomerServiceImpl customerService = new CustomerServiceImpl();

    @Mock
    private Datastore datastore;

    @Before
    public void setUp() {
        FixtureFactoryLoader.loadTemplates("com.acmeair.customer.templates");

        when(datastore.find(CustomerImpl.class)).thenReturn(query);
        when(query.field("_id")).thenReturn(fieldEnd);

        customer = Fixture.from(CustomerImpl.class).gimme("valid");
        address = Fixture.from(CustomerAddressImpl.class).gimme("valid");
    }

    @Test
    public void savesCustomerIntoDatabase() {
        Customer customer = customerService.createCustomer(
                this.customer.getCustomerId(),
                this.customer.getPassword(),
                this.customer.getStatus(),
                this.customer.getTotal_miles(),
                this.customer.getMiles_ytd(),
                this.customer.getPhoneNumber(),
                this.customer.getPhoneNumberType(),
                this.customer.getAddress()
        );

        assertThat(customer, is(this.customer));

        verify(datastore).save(customer);
    }

    @Test
    public void createsCustomerAddress() {
        CustomerAddress address = customerService.createAddress(
                this.address.getStreetAddress1(),
                this.address.getStreetAddress2(),
                this.address.getCity(),
                this.address.getStateProvince(),
                this.address.getCountry(),
                this.address.getPostalCode()
        );

        assertThat(address, is(this.address));
    }

    @Test
    public void clearsPasswordOfRetrievedUser() {
        when(fieldEnd.equal(this.customer.getUsername())).thenReturn(query);
        when(query.get()).thenReturn(customer);

        Customer customer = customerService.getCustomerByUsername(this.customer.getUsername());

        assertThat(customer, is(this.customer));
        assertThat(customer.getPassword(), is(nullValue()));
    }
}
