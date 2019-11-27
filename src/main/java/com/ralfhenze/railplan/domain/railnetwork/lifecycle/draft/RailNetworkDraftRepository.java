package com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft;

import com.ralfhenze.railplan.domain.common.Repository;

import java.util.Optional;

public interface RailNetworkDraftRepository extends Repository {
    Optional<RailNetworkDraft> getRailNetworkDraftOfId(final RailNetworkDraftId id);
    Optional<RailNetworkDraft> persist(final RailNetworkDraft railNetworkDraft);
}
