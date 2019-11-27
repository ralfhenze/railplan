package com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence.dto.RailNetworkDraftDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RailNetworkDraftMongoDbRepository implements RailNetworkDraftRepository {

    public final static String COLLECTION_NAME = "RailNetworkDrafts";

    private final MongoTemplate mongoTemplate;

    @Autowired
    public RailNetworkDraftMongoDbRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<RailNetworkDraft> getRailNetworkDraftOfId(final RailNetworkDraftId id) {
        final RailNetworkDraftDto dto = mongoTemplate
            .findById(id.toString(), RailNetworkDraftDto.class, COLLECTION_NAME);

        return Optional.ofNullable(dto)
            .map(RailNetworkDraftDto::toRailNetworkDraft);
    }

    @Override
    public Optional<RailNetworkDraft> persist(final RailNetworkDraft railNetworkDraft) {
        final RailNetworkDraftDto draftDto = new RailNetworkDraftDto(railNetworkDraft);
        final RailNetworkDraftDto persistedDraftDto = mongoTemplate
            .save(draftDto, COLLECTION_NAME);

        return Optional.of(
            persistedDraftDto.toRailNetworkDraft()
        );
    }
}
