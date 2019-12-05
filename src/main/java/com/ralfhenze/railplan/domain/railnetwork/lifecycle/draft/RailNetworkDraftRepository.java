package com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft;

import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.common.Repository;

import java.util.Optional;

/**
 * A persistence technology agnostic interface to store and retrieve RailNetworkDrafts.
 */
public interface RailNetworkDraftRepository extends Repository {

    /**
     * Loads a RailNetworkDraft from persistence mechanism.
     *
     * @throws EntityNotFoundException if RailNetworkDraft with draftId does not exist
     */
    RailNetworkDraft getRailNetworkDraftOfId(final RailNetworkDraftId draftId);

    /**
     * Stores a RailNetworkDraft in persistence mechanism.
     */
    Optional<RailNetworkDraft> persist(final RailNetworkDraft draft);

    /**
     * Deletes a RailNetworkDraft from persistence mechanism.
     *
     * @throws EntityNotFoundException if RailNetworkDraft with draftId does not exist
     */
    void deleteRailNetworkDraftOfId(final RailNetworkDraftId draftId);
}
