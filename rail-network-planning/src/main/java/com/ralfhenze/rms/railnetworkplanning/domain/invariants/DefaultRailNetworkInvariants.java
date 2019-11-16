package com.ralfhenze.rms.railnetworkplanning.domain.invariants;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class DefaultRailNetworkInvariants {

    public final static Set<Invariant> INVARIANTS = new LinkedHashSet<>(
        Arrays.asList(
            new StationNamesAreUnique(),
            new MinimumDistanceBetweenTwoStationsIs10Km(),
            new MaximumLengthOfTrackIs300Km(),
            new TwoStationsCanOnlyBeConnectedByOneTrack()
        )
    );
}
