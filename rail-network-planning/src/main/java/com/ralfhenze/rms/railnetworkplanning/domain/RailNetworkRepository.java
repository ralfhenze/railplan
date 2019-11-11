package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Repository;

import java.time.LocalDate;
import java.util.Optional;

interface RailNetworkRepository extends Repository {
    Optional<RailNetwork> getRailNetworkOfId(RailNetworkId id);

    Optional<RailNetwork> getLastReleasedRailNetwork();
    // Or more performant (because that's everything we need for consistent state):
    Optional<LocalDate> getEndDateOfLastReleasedRailNetwork();

    void persist(RailNetwork railNetwork);
}
