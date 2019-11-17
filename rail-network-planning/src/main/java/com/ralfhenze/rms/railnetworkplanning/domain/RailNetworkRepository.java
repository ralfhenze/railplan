package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Repository;

import java.util.Optional;

public interface RailNetworkRepository extends Repository {
    Optional<RailNetwork> getLastReleasedRailNetwork();
    Optional<RailNetwork> add(RailNetwork railNetwork);
}
