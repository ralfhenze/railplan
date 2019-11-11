package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Repository;

import java.util.Optional;

interface RailNetworkDraftRepository extends Repository {
    Optional<RailNetworkDraft> getRailNetworkDraftOfId(RailNetworkDraftId id);
    void persist(RailNetworkDraft railNetworkDraft);
}
