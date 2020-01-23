package com.ralfhenze.railplan.userinterface.web;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

/**
 * This is needed to prevent the default "_class" field of Spring Data's MongoDB implementation
 *
 * https://docs.spring.io/spring-data/data-mongodb/docs/current/reference/html/#mongo-template.type-mapping
 */
@Configuration
public class MongoConfiguration extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private String port;

    @Value("${spring.data.mongodb.username}")
    private String username;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    public MongoClient mongoClient() {
        var user = "";
        if (!username.isBlank()) {
            user = username + ":" + password + "@";
        }

        return MongoClients.create("mongodb://" + user + host + ":" + port);
    }

    @Bean
    @Override
    public MappingMongoConverter mappingMongoConverter() throws Exception {
        final var mmc = super.mappingMongoConverter();
        // the null argument actually disables the "_class" field:
        final var typeMapper = new DefaultMongoTypeMapper(null);
        mmc.setTypeMapper(typeMapper);

        return mmc;
    }
}
