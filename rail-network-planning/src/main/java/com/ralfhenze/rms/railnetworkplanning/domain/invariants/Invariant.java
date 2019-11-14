package com.ralfhenze.rms.railnetworkplanning.domain.invariants;

import com.ralfhenze.rms.railnetworkplanning.domain.DoubleTrackRailway;
import com.ralfhenze.rms.railnetworkplanning.domain.station.TrainStation;

import java.util.Set;

public interface Invariant {
    void ensureIsSatisfied(Set<TrainStation> stations, Set<DoubleTrackRailway> connections);
}
