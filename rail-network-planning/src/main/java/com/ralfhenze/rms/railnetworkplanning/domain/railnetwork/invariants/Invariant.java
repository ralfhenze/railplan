package com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.invariants;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStation;
import org.eclipse.collections.api.set.ImmutableSet;

public interface Invariant {
    void ensureIsSatisfied(
        final ImmutableSet<TrainStation> stations,
        final ImmutableSet<RailwayTrack> tracks
    );
}
