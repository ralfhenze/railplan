package com.ralfhenze.railplan.domain.railnetwork.lifecycle.release;

import com.ralfhenze.railplan.domain.common.Repository;

import java.util.Optional;

public interface ReleasedRailNetworkRepository extends Repository {
    Optional<ReleasedRailNetwork> getLastReleasedRailNetwork();
    Optional<ReleasedRailNetwork> add(ReleasedRailNetwork railNetwork);
}
