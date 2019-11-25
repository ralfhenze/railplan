package com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.release.ReleasedRailNetwork;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.release.ReleasedRailNetworkRepository;
import com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence.dto.RailNetworkDraftDto;
import com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence.dto.ReleasedRailNetworkDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ReleasedRailNetworkMongoDbRepository implements ReleasedRailNetworkRepository {

    private final static String COLLECTION_NAME = "ReleasedRailNetworks";

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Optional<ReleasedRailNetwork> getLastReleasedRailNetwork() {
        final Query sortedByStartDateQuery = new Query();
        sortedByStartDateQuery.with(Sort.by(Sort.Direction.DESC, "startDate"));

        final ReleasedRailNetworkDto dto = mongoTemplate
            .findOne(sortedByStartDateQuery, ReleasedRailNetworkDto.class, COLLECTION_NAME);

        return Optional.ofNullable(dto)
            .map(ReleasedRailNetworkDto::toReleasedRailNetwork);
    }

    @Override
    public Optional<ReleasedRailNetwork> add(ReleasedRailNetwork railNetwork) {
        // TODO: only accept networks without ID
        final ReleasedRailNetworkDto networkDto = new ReleasedRailNetworkDto(railNetwork);
        final ReleasedRailNetworkDto persistedNetworkDto = mongoTemplate
            .insert(networkDto, COLLECTION_NAME);

        return Optional.of(
            persistedNetworkDto.toReleasedRailNetwork()
        );
    }
}
