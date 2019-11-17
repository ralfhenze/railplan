package com.ralfhenze.rms.railnetworkplanning.domain.invariants;

import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.set.ImmutableSet;

public class DefaultRailNetworkInvariants {

    public final static ImmutableSet<Invariant> INVARIANTS = Sets.immutable.of(
        new StationNamesAreUnique(),
        new MinimumDistanceBetweenTwoStationsIs10Km(),
        new MaximumLengthOfTrackIs300Km(),
        new TwoStationsCanOnlyBeConnectedByOneTrack()
    );
}
