package com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence;

import com.ralfhenze.rms.railnetworkplanning.application.queries.Queries;
import com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence.dto.RailNetworkDraftDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MongoDbQueries implements Queries {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoDbQueries(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<String> getAllDraftIds() {
        return mongoTemplate
            .findAll(RailNetworkDraftDto.class, RailNetworkDraftMongoDbRepository.COLLECTION_NAME)
            .stream()
            .map(RailNetworkDraftDto::getId)
            .collect(Collectors.toList());
    }
}
