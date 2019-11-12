package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.invariants.*;
import com.ralfhenze.rms.railnetworkplanning.domain.station.StationId;
import com.ralfhenze.rms.railnetworkplanning.domain.station.TrainStation;

import java.util.*;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

public class RailNetworkGraph {

    final Map<StationId, TrainStation> stations = new HashMap<>();
    final Set<DoubleTrackRailway> connections = new HashSet<>();

    final Set<Invariant> defaultInvariants = new HashSet<>(
        Arrays.asList(
            new MaximumLengthOfTrackIs200Km(),
            new MinimumDistanceBetweenTwoStationsIs10Km(),
            new StationNamesAreUnique(),
            new TwoStationsCanOnlyBeConnectedByOneTrack()
        )
    );

    final Set<Invariant> invariants;

    RailNetworkGraph() {
        this.invariants = this.defaultInvariants;
    }

    RailNetworkGraph(final Set<Invariant> additionalInvariants) {
        ensureNotNull(additionalInvariants, "Additional invariants are required");

        // TODO: improve this when moved to Java 11
        Set<Invariant> invariants = new HashSet<>(this.defaultInvariants);
        invariants.addAll(additionalInvariants);
        this.invariants = invariants;
    }
}
