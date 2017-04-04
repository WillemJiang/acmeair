package com.acmeair.web;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.acmeair.entities.Customer;
import com.acmeair.morphia.entities.CustomerImpl;
import com.acmeair.service.CustomerService;
import com.acmeair.web.dto.CustomerInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.seanyinx.github.unit.scaffolding.Randomness.uniquify;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.COOKIE;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "dev"})
public class CustomerRESTTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private CustomerService customerService;

    @Autowired
    private TestRestTemplate restTemplate;

    private CustomerInfo customerInfo;
    private Customer customer;

    @Before
    public void setUp() throws Exception {
        FixtureFactoryLoader.loadTemplates("com.acmeair.customer.templates");

        customer = Fixture.from(CustomerImpl.class).gimme("valid");
        customerInfo = new CustomerInfo(customer);
    }

    @Test
    public void getsCustomerWithUnderlyingService() {
        when(customerService.getCustomerByUsername(customer.getUsername())).thenReturn(customer);

        ResponseEntity<CustomerInfo> responseEntity = restTemplate.getForEntity("/rest/api/customer/{custid}", CustomerInfo.class, customer.getUsername());

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody(), is(customerInfo));
    }

    @Test
    public void forbidsOneCustomerToGetInfoOfAnother() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(COOKIE, "sessionid=" + uniquify("session-id"));
        headers.set(CustomerREST.LOGIN_USER, uniquify("wrong user id"));

        ResponseEntity<CustomerInfo> responseEntity = restTemplate.exchange(
                "/rest/api/customer/byid/{custid}",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CustomerInfo.class,
                customer.getUsername());

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void getsCustomerInfoOfOneself() {
        when(customerService.getCustomerByUsername(customer.getUsername())).thenReturn(customer);

        HttpHeaders headers = new HttpHeaders();
        headers.set(COOKIE, "sessionid=" + uniquify("session-id"));
        headers.set(CustomerREST.LOGIN_USER, customer.getUsername());

        ResponseEntity<CustomerInfo> responseEntity = restTemplate.exchange(
                "/rest/api/customer/byid/{custid}",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CustomerInfo.class,
                customer.getUsername());

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody(), is(customerInfo));
    }

    @Test
    public void forbidsOneCustomerToUpdateInfoOfAnother() throws JsonProcessingException {
        HttpEntity<CustomerInfo> requestEntity = postRequestOfUser(uniquify("wrong user id"));

        ResponseEntity<CustomerInfo> responseEntity = restTemplate.exchange(
                "/rest/api/customer/byid/{custid}",
                HttpMethod.POST,
                requestEntity,
                CustomerInfo.class,
                customer.getUsername());

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void forbidsCustomerToUpdateInfoIfPasswordMismatch() throws JsonProcessingException {
        HttpEntity<CustomerInfo> requestEntity = postRequestOfUser(customer.getUsername());

        ResponseEntity<CustomerInfo> responseEntity = restTemplate.exchange(
                "/rest/api/customer/byid/{custid}",
                HttpMethod.POST,
                requestEntity,
                CustomerInfo.class,
                customer.getUsername());

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void updatesCustomerInfoOfOneself() throws JsonProcessingException {
        when(customerService.getCustomerByUsernameAndPassword(customer.getUsername(), customer.getPassword())).thenReturn(customer);

        HttpEntity<CustomerInfo> requestEntity = postRequestOfUser(customer.getUsername());

        ResponseEntity<CustomerInfo> responseEntity = restTemplate.exchange(
                "/rest/api/customer/byid/{custid}",
                HttpMethod.POST,
                requestEntity,
                CustomerInfo.class,
                customer.getUsername());

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().getPassword(), is(nullValue()));
        verify(customerService).updateCustomer(customer);
    }

    private HttpEntity<CustomerInfo> postRequestOfUser(String username) {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(COOKIE, "sessionid=" + uniquify("session-id"));
        headers.set(CustomerREST.LOGIN_USER, username);

        return new HttpEntity<>(customerInfo, headers);
    }
}