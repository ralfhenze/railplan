package com.ralfhenze.rms.railnetworkplanning.domain;

import java.time.LocalDate;
import java.util.Optional;

interface ReleasedRailNetworkRepository {
    Optional<ReleasedRailNetwork> getReleasedRailNetworkOfId(RailNetworkId id);

    Optional<ReleasedRailNetwork> getLastReleasedRailNetwork();
    // Or more performant (because that's everything we need for consistent state):
    Optional<LocalDate> getEndDateOfLastReleasedRailNetwork();

    void persist(ReleasedRailNetwork railNetwork);
}
