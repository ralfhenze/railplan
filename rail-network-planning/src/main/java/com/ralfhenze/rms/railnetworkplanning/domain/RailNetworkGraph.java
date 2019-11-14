package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.invariants.*;
import com.ralfhenze.rms.railnetworkplanning.domain.station.StationId;
import com.ralfhenze.rms.railnetworkplanning.domain.station.StationName;
import com.ralfhenze.rms.railnetworkplanning.domain.station.TrainStation;

import java.util.*;
import java.util.stream.Collectors;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

public class RailNetworkGraph {

    private final Set<TrainStation> stations;
    //private final Map<StationId, TrainStation> stations;
    private final Set<DoubleTrackRailway> connections;

    private final static Set<Invariant> DEFAULT_INVARIANTS = new LinkedHashSet<>(
        Arrays.asList(
            new MaximumLengthOfTrackIs200Km(),
            new MinimumDistanceBetweenTwoStationsIs10Km(),
            new StationNamesAreUnique(),
            new TwoStationsCanOnlyBeConnectedByOneTrack()
        )
    );

    private final Set<Invariant> invariants;

    RailNetworkGraph(
        final Set<TrainStation> stations,
        final Set<DoubleTrackRailway> connections
    ) {
        this(stations, connections, new LinkedHashSet<>());
    }

    RailNetworkGraph(
        final Set<TrainStation> stations,
        final Set<DoubleTrackRailway> connections,
        final Set<Invariant> additionalInvariants
    ) {
        this.stations = ensureNotNull(stations, "Train Stations");
            //.stream().collect(Collectors.toMap(TrainStation::getId, t -> t));
        this.connections = ensureNotNull(connections, "Connections");

        ensureNotNull(additionalInvariants, "Additional Invariants");
        this.invariants = new LinkedHashSet<>(DEFAULT_INVARIANTS);
        // TODO: ensure no null invariant in the Set
        this.invariants.addAll(additionalInvariants);
    }

    RailNetworkGraph withAdditionalInvariant(final Invariant additionalInvariant) {
        ensureNotNull(additionalInvariant, "Additional Invariant");

        Set<Invariant> additionalInvariants = new LinkedHashSet<>(invariants);
        additionalInvariants.add(additionalInvariant);

        return new RailNetworkGraph(stations, connections, additionalInvariants);
    }

    public void ensureInvariants() {
        for (final Invariant invariant : invariants) {
            invariant.ensureIsSatisfied(stations, connections);
        }
    }
}
