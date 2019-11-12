package com.ralfhenze.rms.railnetworkplanning.domain.invariants;

public class TwoStationsCanOnlyBeConnectedByOneTrack implements Invariant {

    @Override
    public boolean isSatisfied() {
        return true;
    }
}
