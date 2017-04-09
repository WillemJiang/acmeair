package com.acmeair.morphia.services;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.acmeair.entities.Customer;
import com.acmeair.entities.CustomerAddress;
import com.acmeair.entities.CustomerSession;
import com.acmeair.morphia.entities.CustomerAddressImpl;
import com.acmeair.morphia.entities.CustomerImpl;
import com.acmeair.morphia.entities.CustomerSessionImpl;
import com.acmeair.morphia.repositories.CustomerRepository;
import com.acmeair.morphia.repositories.CustomerSessionRepository;
import com.acmeair.service.KeyGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static com.seanyinx.github.unit.scaffolding.Randomness.uniquify;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceImplTest {
    private CustomerAddress     address;
    private CustomerImpl        customer;
    private CustomerSessionImpl customerSession;

    @InjectMocks
    private final CustomerServiceImpl customerService = new CustomerServiceImpl();

    @Mock
    private CustomerRepository        customerRepository;
    @Mock
    private CustomerSessionRepository sessionRepository;

    @Mock
    private KeyGenerator keyGenerator;

    private String sessionId = uniquify("sessionId");

    @Before
    public void setUp() {
        FixtureFactoryLoader.loadTemplates("com.acmeair.customer.templates");

        when(keyGenerator.generate()).thenReturn(sessionId);

        customer = Fixture.from(CustomerImpl.class).gimme("valid");
        address = Fixture.from(CustomerAddressImpl.class).gimme("valid");
        customerSession = Fixture.from(CustomerSessionImpl.class).gimme("valid");
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

        verify(customerRepository).save((CustomerImpl) customer);
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
        when(customerRepository.findOne(customer.getCustomerId())).thenReturn(customer);

        Customer customer = customerService.getCustomerByUsername(this.customer.getUsername());

        assertThat(customer, is(this.customer));
        assertThat(customer.getPassword(), is(nullValue()));
    }

    @Test
    public void getsCustomerIfUsernameAndPasswordMatches() {
        when(customerRepository.findOne(customer.getCustomerId())).thenReturn(this.customer);

        Customer c = customerService.getCustomerByUsernameAndPassword(customer.getUsername(), customer.getPassword());

        assertThat(c, is(customer));
    }

    @Test
    public void getsNullIfUsernameAndPasswordDoesNotMatch() {
        when(customerRepository.findOne(customer.getCustomerId())).thenReturn(this.customer);

        Customer c = customerService.getCustomerByUsernameAndPassword(customer.getUsername(), uniquify("wrong password"));

        assertThat(c, is(nullValue()));
    }

    @Test
    public void getsNullIfUserDoesNotExist() {
        Customer c = customerService.getCustomerByUsernameAndPassword(uniquify("unknown user"), uniquify("wrong password"));

        assertThat(c, is(nullValue()));
    }

    @Test
    public void validCustomerIfUsernameAndPasswordMatches() {
        when(customerRepository.findOne(customer.getCustomerId())).thenReturn(this.customer);

        boolean isValid = customerService.validateCustomer(customer.getUsername(), customer.getPassword());

        assertThat(isValid, is(true));
    }

    @Test
    public void inValidCustomerIfUsernameAndPasswordDoesNotMatch() {
        when(customerRepository.findOne(customer.getCustomerId())).thenReturn(this.customer);

        boolean isValid = customerService.validateCustomer(customer.getUsername(), uniquify("wrong password"));

        assertThat(isValid, is(false));
    }

    @Test
    public void inValidCustomerIfUserDoesNotExist() {
        boolean isValid = customerService.validateCustomer(uniquify("unknown user"), uniquify("wrong password"));

        assertThat(isValid, is(false));
    }

    @Test
    public void savesSessionIntoDatabase() {
        CustomerSession session = customerService.createSession(customer.getCustomerId());

        Date now = new Date();

        assertThat(session.getId(), is(sessionId));
        assertThat(session.getCustomerid(), is(customer.getCustomerId()));
        assertThat(session.getTimeoutTime().after(now), is(true));
        verify(sessionRepository).save((CustomerSessionImpl) session);
    }

    @Test
    public void deletesSessionFromDatabaseWhenInvalidated() {
        customerService.invalidateSession(sessionId);

        verify(sessionRepository).delete(sessionId);
    }

    @Test
    public void validSessionIfExistingInDatabaseAndNotExpired() {
        when(sessionRepository.findOne(customerSession.getId())).thenReturn(customerSession);

        CustomerSession session = customerService.validateSession(customerSession.getId());

        assertThat(session, is(customerSession));
    }

    @Test
    public void nullSessionIfExistingInDatabaseAndButExpired() {
        when(sessionRepository.findOne(customerSession.getId())).thenReturn(Fixture.from(CustomerSessionImpl.class).gimme("expired"));

        CustomerSession session = customerService.validateSession(customerSession.getId());

        assertThat(session, is(nullValue()));
    }

    @Test
    public void nullSessionIfNotFoundInDatabase() {
        CustomerSession session = customerService.validateSession(customerSession.getId());

        assertThat(session, is(nullValue()));
    }
}
