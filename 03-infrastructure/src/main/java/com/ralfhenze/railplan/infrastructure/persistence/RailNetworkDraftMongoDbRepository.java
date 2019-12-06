package com.ralfhenze.railplan.infrastructure.persistence;

import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDraftDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A Mongo DB persistence implementation to store and retrieve RailNetworkDrafts.
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
     * Loads a RailNetworkDraft from Mongo DB.
     *
     * @throws EntityNotFoundException if RailNetworkDraft with draftId does not exist
     */
    @Override
    public RailNetworkDraft getRailNetworkDraftOfId(final RailNetworkDraftId draftId) {
        final var draftDto = mongoTemplate
            .findById(draftId.toString(), RailNetworkDraftDto.class, COLLECTION_NAME);

        if (draftDto == null) {
            throw new EntityNotFoundException("RailNetworkDraft", draftId.toString());
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

    /**
     * Deletes a RailNetworkDraft from Mongo DB.
     *
     * @throws EntityNotFoundException if RailNetworkDraft with draftId does not exist
     */
    @Override
    public void deleteRailNetworkDraftOfId(final RailNetworkDraftId draftId) {
        final var numberOfDeletedDrafts = mongoTemplate.remove(
            new Query(Criteria.where("id").is(draftId.toString())),
            RailNetworkDraftDto.class,
            COLLECTION_NAME
        ).getDeletedCount();

        if (numberOfDeletedDrafts == 0) {
            throw new EntityNotFoundException("RailNetworkDraft", draftId.toString());
        }
    }
}
