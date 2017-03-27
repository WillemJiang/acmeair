package com.acmeair.morphia.services.util;

import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/acmeair-mongo.properties")
class MongoDataSourceConfiguration {

    @Value("${mongo.host}")
    private String host;

    @Value("${mongo.port}")
    private String port;

    @Value("${mongo.database}")
    private String database;

    @Bean
    Datastore datastore() {
        return new MongoConnectionManager(host, port, database).getDatastore();
    }
}
