package com.ralfhenze.railplan.infrastructure.persistence;

import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDraftDto;
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
        final var draftDto = mongoTemplate
            .findById(id.toString(), RailNetworkDraftDto.class, COLLECTION_NAME);

        return Optional.ofNullable(draftDto)
            .map(RailNetworkDraftDto::toRailNetworkDraft);
    }

    @Override
    public Optional<RailNetworkDraft> persist(final RailNetworkDraft railNetworkDraft) {
        final var draftDto = new RailNetworkDraftDto(railNetworkDraft);
        final var persistedDraftDto = mongoTemplate.save(draftDto, COLLECTION_NAME);

        return Optional.of(
            persistedDraftDto.toRailNetworkDraft()
        );
    }
}
