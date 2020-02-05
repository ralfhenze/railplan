package com.ralfhenze.railplan.infrastructure.persistence;

import com.ralfhenze.railplan.application.queries.Queries;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDraftDto;
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

    public List<RailNetworkDraftDto> getAllDrafts() {
        return mongoTemplate
            .findAll(RailNetworkDraftDto.class, RailNetworkDraftMongoDbRepository.COLLECTION_NAME);
    }

    public RailNetworkDraftDto getDraftOfId(String id) {
        return mongoTemplate.findById(
            id,
            RailNetworkDraftDto.class,
            RailNetworkDraftMongoDbRepository.COLLECTION_NAME
        );
    }
}
