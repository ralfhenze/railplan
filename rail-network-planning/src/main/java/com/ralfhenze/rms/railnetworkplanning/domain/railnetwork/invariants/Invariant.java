package com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.invariants;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStation;
import org.eclipse.collections.api.list.ImmutableList;

public interface Invariant {
    void ensureIsSatisfied(
        final ImmutableList<TrainStation> stations,
        final ImmutableList<RailwayTrack> tracks
    );
}
