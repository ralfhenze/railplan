package com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.invariants;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.DoubleTrackRailway;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStation;

import java.util.Set;

public interface Invariant {
    void ensureIsSatisfied(Set<TrainStation> stations, Set<DoubleTrackRailway> connections);
}
