package com.ralfhenze.rms.railnetworkplanning.domain;

import java.util.Optional;

interface RailNetworkDraftRepository {
    Optional<RailNetworkDraft> getRailNetworkDraftOfId(RailNetworkDraftId id);
    void persist(RailNetworkDraft railNetworkDraft);
}
