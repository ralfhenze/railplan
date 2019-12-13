package com.ralfhenze.railplan.infrastructure.persistence;

import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetwork;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetworkRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.ReleasedRailNetworkDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ReleasedRailNetworkMongoDbRepository implements ReleasedRailNetworkRepository {

    public final static String COLLECTION_NAME = "ReleasedRailNetworks";

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ReleasedRailNetworkMongoDbRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<ReleasedRailNetwork> getLastReleasedRailNetwork() {
        final var sortedByStartDateQuery = new Query();
        sortedByStartDateQuery.with(Sort.by(Sort.Direction.DESC, "startDate"));

        final var networkDto = mongoTemplate
            .findOne(sortedByStartDateQuery, ReleasedRailNetworkDto.class, COLLECTION_NAME);

        return Optional.ofNullable(networkDto)
            .map(ReleasedRailNetworkDto::toReleasedRailNetwork);
    }

    @Override
    public ReleasedRailNetwork add(ReleasedRailNetwork railNetwork) {
        // TODO: only accept networks without ID
        final var networkDto = new ReleasedRailNetworkDto(railNetwork);
        final var persistedNetworkDto = mongoTemplate.insert(networkDto, COLLECTION_NAME);

        return persistedNetworkDto.toReleasedRailNetwork();
    }
}
