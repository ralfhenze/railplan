package com.ralfhenze.railplan.domain.railnetwork.invariants;

import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.Collection;

public class HasUniqueStationNames implements ValidationConstraint<ImmutableList<TrainStation>> {

    private TrainStationName duplicateStationName;

    @Override
    public boolean isValid(final ImmutableList<TrainStation> stations) {
        duplicateStationName = null;

        if (stations.size() >= 2) {
            duplicateStationName = stations
                .groupBy(TrainStation::getName)
                .selectKeysMultiValues((name, s) -> ((Collection) s).size() > 1)
                .keyBag()
                .getAny();
        }

        return (duplicateStationName == null);
    }

    @Override
    public String getErrorMessage(
        final String fieldName,
        final ImmutableList<TrainStation> stations
    ) {
        return "Station Name \"" + duplicateStationName + "\" already exists";
    }
}
