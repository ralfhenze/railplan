package com.ralfhenze.railplan.infrastructure.persistence;

import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.railnetwork.RailNetwork;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkId;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A Mongo DB persistence implementation to store and retrieve RailNetworks.
 */
@Repository
public class RailNetworkMongoDbRepository implements RailNetworkRepository {

    public final static String COLLECTION_NAME = "RailNetworks";

    private final MongoTemplate mongoTemplate;

    @Autowired
    public RailNetworkMongoDbRepository(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Loads a RailNetwork from Mongo DB.
     *
     * @throws EntityNotFoundException if RailNetwork with networkId does not exist
     */
    @Override
    public RailNetwork getRailNetworkOfId(final RailNetworkId networkId) {
        final var networkDto = mongoTemplate
            .findById(networkId.toString(), RailNetworkDto.class, COLLECTION_NAME);

        if (networkDto == null) {
            throw new EntityNotFoundException("RailNetwork", networkId.toString());
        }

        return networkDto.toRailNetwork();
    }

    @Override
    public Optional<RailNetwork> persist(final RailNetwork railNetwork) {
        final var networkDto = new RailNetworkDto(railNetwork);
        final var persistedNetworkDto = mongoTemplate.save(networkDto, COLLECTION_NAME);

        return Optional.of(
            persistedNetworkDto.toRailNetwork()
        );
    }

    /**
     * Deletes a RailNetwork from Mongo DB.
     *
     * @throws EntityNotFoundException if RailNetwork with networkId does not exist
     */
    @Override
    public void deleteRailNetworkOfId(final RailNetworkId networkId) {
        final var numberOfDeletedNetworks = mongoTemplate.remove(
            new Query(Criteria.where("id").is(networkId.toString())),
            RailNetworkDto.class,
            COLLECTION_NAME
        ).getDeletedCount();

        if (numberOfDeletedNetworks == 0) {
            throw new EntityNotFoundException("RailNetwork", networkId.toString());
        }
    }
}
