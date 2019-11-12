package com.ralfhenze.rms.railnetworkplanning.domain.invariants;

public class StationNamesAreUnique implements Invariant {

    @Override
    public boolean isSatisfied() {
        return true;
    }
}
