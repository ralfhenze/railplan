package com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Repository;

import java.util.Optional;

public interface RailNetworkDraftRepository extends Repository {
    Optional<RailNetworkDraft> getRailNetworkDraftOfId(final RailNetworkDraftId id);
    Optional<RailNetworkDraft> persist(final RailNetworkDraft railNetworkDraft);
}
