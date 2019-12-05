package com.ralfhenze.railplan.infrastructure.persistence;

import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDraftDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A Mongo DB persistence implementation to store and retrieve Rail Network Drafts
 */
@Repository
public class RailNetworkDraftMongoDbRepository implements RailNetworkDraftRepository {

    public final static String COLLECTION_NAME = "RailNetworkDrafts";

    private final MongoTemplate mongoTemplate;

    @Autowired
    public RailNetworkDraftMongoDbRepository(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Loads a Rail Network Draft from Mongo DB
     *
     * @throws EntityNotFoundException if Rail Network Draft with draftId does not exist
     */
    @Override
    public RailNetworkDraft getRailNetworkDraftOfId(final RailNetworkDraftId draftId) {
        final var draftDto = mongoTemplate
            .findById(draftId.toString(), RailNetworkDraftDto.class, COLLECTION_NAME);

        if (draftDto == null) {
            throw new EntityNotFoundException("Rail Network Draft", draftId.toString());
        }

        return draftDto.toRailNetworkDraft();
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
