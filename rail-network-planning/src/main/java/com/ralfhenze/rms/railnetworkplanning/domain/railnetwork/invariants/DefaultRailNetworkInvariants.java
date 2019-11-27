package com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.invariants;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;

public class DefaultRailNetworkInvariants {

    public final static ImmutableList<Invariant> INVARIANTS = Lists.immutable.of(
        new StationNamesAreUnique(),
        new MinimumDistanceBetweenTwoStationsIs10Km(),
        new MaximumLengthOfTrackIs300Km(),
        new TwoStationsCanOnlyBeConnectedByOneTrack()
    );
}
