package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Repository;

import java.util.Optional;

public interface RailNetworkDraftRepository extends Repository {
    Optional<RailNetworkDraft> getRailNetworkDraftOfId(RailNetworkDraftId id);
    Optional<RailNetworkDraft> persist(RailNetworkDraft railNetworkDraft);
}
