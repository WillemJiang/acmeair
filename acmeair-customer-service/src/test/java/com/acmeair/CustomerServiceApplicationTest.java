package com.acmeair;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import org.bson.Document;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.COOKIE;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = CustomerServiceApplication.class,
        webEnvironment = RANDOM_PORT,
        properties = {
                "spring.data.mongodb.host=localhost",
                "spring.data.mongodb.port=27017"
        })
@ActiveProfiles("test")
public class CustomerServiceApplicationTest {
    private static final MongodStarter starter = MongodStarter.getDefaultInstance();
    private static MongodExecutable mongodExecutable;
    private static MongodProcess    mongodProcess;
    private static MongoClient      mongoClient;

    private final String      customerId = "mike";
    private final String      password   = "password";

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Autowired
    private TestRestTemplate restTemplate;
    private MongoDatabase    database;

    @BeforeClass
    public static void setUpClass() throws Exception {
        // We don't start the mongoDB, if the system property mongodb.start is false.
        if (Boolean.valueOf(System.getProperty("mongod.start", "true"))) {
            mongodExecutable = starter.prepare(new MongodConfigBuilder().version(Version.Main.PRODUCTION).net(new Net("localhost", 27017, false)).build());

            mongodProcess = mongodExecutable.start();
        }
        mongoClient = new MongoClient("localhost", 27017);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        mongoClient.close();
        if (mongodProcess != null) {
            mongodProcess.stop();
        }
        if (mongodExecutable != null) {
            mongodExecutable.stop();
        }
    }

    @Before
    public void setUp() {
        database = mongoClient.getDatabase(databaseName);

        addDocumentToCollection("customer", new HashMap<String, Object>() {{
            put("_id", customerId);
            put("password", password);
        }});
    }

    @After
    public void tearDown() throws Exception {
        database.drop();
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
                loginRequest(customerId, password),
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

    private void addDocumentToCollection(String collectionName, HashMap<String, Object> map) {
        database.createCollection(collectionName);
        database.getCollection(collectionName).insertOne(new Document(map));
    }
}
