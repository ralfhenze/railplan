package com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft;

import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.common.Repository;

import java.util.Optional;

/**
 * A persistence technology agnostic interface to store and retrieve Rail Network Drafts
 */
public interface RailNetworkDraftRepository extends Repository {

    /**
     * Loads a Rail Network Draft from persistence mechanism
     *
     * @throws EntityNotFoundException if Rail Network Draft with draftId does not exist
     */
    RailNetworkDraft getRailNetworkDraftOfId(final RailNetworkDraftId draftId);

    Optional<RailNetworkDraft> persist(final RailNetworkDraft draft);
}
