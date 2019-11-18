package com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.release;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Repository;

import java.util.Optional;

public interface ReleasedRailNetworkRepository extends Repository {
    Optional<ReleasedRailNetwork> getLastReleasedRailNetwork();
    Optional<ReleasedRailNetwork> add(ReleasedRailNetwork railNetwork);
}
