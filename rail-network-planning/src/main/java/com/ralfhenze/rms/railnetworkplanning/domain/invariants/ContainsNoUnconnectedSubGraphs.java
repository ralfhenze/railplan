package com.ralfhenze.rms.railnetworkplanning.domain.invariants;

public class ContainsNoUnconnectedSubGraphs implements Invariant {

    @Override
    public boolean isSatisfied() {
        return true;
    }
}
