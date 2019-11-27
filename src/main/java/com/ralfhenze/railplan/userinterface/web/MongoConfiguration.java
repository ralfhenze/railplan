package com.ralfhenze.railplan.userinterface.web;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

/**
 * This is needed to prevent the default "_class" field of Spring Data's Mongo implementation
 *
 * https://docs.spring.io/spring-data/data-mongodb/docs/current/reference/html/#mongo-template.type-mapping
 */
@Configuration
public class MongoConfiguration extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "rms";
    }

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create();
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
