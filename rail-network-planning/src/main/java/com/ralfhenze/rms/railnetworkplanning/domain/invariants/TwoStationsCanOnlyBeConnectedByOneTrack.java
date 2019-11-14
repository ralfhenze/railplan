package com.ralfhenze.rms.railnetworkplanning.domain.invariants;

import com.ralfhenze.rms.railnetworkplanning.domain.DoubleTrackRailway;
import com.ralfhenze.rms.railnetworkplanning.domain.station.TrainStation;

import java.util.Set;

public class TwoStationsCanOnlyBeConnectedByOneTrack implements Invariant {

    @Override
    public void ensureIsSatisfied(Set<TrainStation> stations, Set<DoubleTrackRailway> connections) {
    }
}
