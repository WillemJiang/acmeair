package com.acmeair;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.acmeair.morphia.entities.CustomerImpl;
import com.acmeair.morphia.repositories.CustomerRepository;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.http.HttpHeaders.COOKIE;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
public class CustomerServiceApplicationTestBase {
    private static ConfigurableApplicationContext applicationContext;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ConfigurableApplicationContext context;

    private CustomerImpl customer;

    @AfterClass
    public static void tearDownClass() throws Exception {
        applicationContext.close();
    }

    @Before
    public void setUp() {
        FixtureFactoryLoader.loadTemplates("com.acmeair.customer.templates");

        customer = Fixture.from(CustomerImpl.class).gimme("valid");

        customerRepository.save(customer);
    }

    @After
    public void tearDown() throws Exception {
        applicationContext = context;
    }

    @Test
    public void countsNumberOfConsumers() {
        HttpHeaders headers = headerWithCookieOfLoginSession();

        ResponseEntity<Long> consumerCount = restTemplate.exchange(
                "/rest/info/config/countCustomers",
                GET,
                new HttpEntity<>(headers),
                Long.class
        );

        assertThat(consumerCount.getStatusCode(), is(OK));
        assertThat(consumerCount.getBody(), is(1L));
    }

    private HttpHeaders headerWithCookieOfLoginSession() {
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                "/rest/api/login",
                loginRequest(customer.getCustomerId(), customer.getPassword()),
                String.class
        );

        assertThat(responseEntity.getStatusCode(), is(OK));

        List<String> cookies = responseEntity.getHeaders().get(SET_COOKIE);
        assertThat(cookies, is(notNullValue()));

        HttpHeaders headers = new HttpHeaders();
        headers.set(COOKIE, String.join(";", cookies));
        return headers;
    }

    private HttpEntity<MultiValueMap<String, String>> loginRequest(String login, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("login", login);
        map.add("password", password);

        return new HttpEntity<>(map, headers);
    }
}
