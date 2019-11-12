package com.ralfhenze.rms.railnetworkplanning.domain.invariants;

public class ContainsAtLeastTwoStationsAndOneTrack implements Invariant {

    @Override
    public boolean isSatisfied() {
        return true;
    }
}
