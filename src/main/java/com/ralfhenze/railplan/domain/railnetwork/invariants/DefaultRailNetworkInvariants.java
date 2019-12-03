package com.ralfhenze.railplan.domain.railnetwork.invariants;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;

public class DefaultRailNetworkInvariants {

    public final static ImmutableList<Invariant> INVARIANTS = Lists.immutable.of(
        new MaximumLengthOfTrackIs300Km(),
        new TwoStationsCanOnlyBeConnectedByOneTrack()
    );
}
