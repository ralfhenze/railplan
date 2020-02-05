package com.ralfhenze.railplan.infrastructure.persistence;

import com.ralfhenze.railplan.application.queries.Queries;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDto;
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
    public List<String> getAllNetworkIds() {
        return mongoTemplate
            .findAll(RailNetworkDto.class, RailNetworkMongoDbRepository.COLLECTION_NAME)
            .stream()
            .map(RailNetworkDto::getId)
            .collect(Collectors.toList());
    }

    public List<RailNetworkDto> getAllNetworks() {
        return mongoTemplate
            .findAll(RailNetworkDto.class, RailNetworkMongoDbRepository.COLLECTION_NAME);
    }

    public RailNetworkDto getNetworkOfId(String id) {
        return mongoTemplate.findById(
            id,
            RailNetworkDto.class,
            RailNetworkMongoDbRepository.COLLECTION_NAME
        );
    }
}
