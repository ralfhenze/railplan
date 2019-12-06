package com.ralfhenze.railplan.domain.railnetwork.invariants;

import com.ralfhenze.railplan.domain.common.validation.ErrorMessage;
import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.Collection;
import java.util.Optional;

public class HasUniqueStationNames implements ValidationConstraint<ImmutableList<TrainStation>> {

    @Override
    public Optional<ErrorMessage> validate(
        final ImmutableList<TrainStation> stations,
        final String fieldName
    ) {
        if (stations.size() >= 2) {
            final var duplicateStationName = stations
                .groupBy(TrainStation::getName)
                .selectKeysMultiValues((name, s) -> ((Collection) s).size() > 1)
                .keyBag()
                .getAny();

            if (duplicateStationName != null) {
                return Optional.of(
                    new ErrorMessage(
                        "Station Name \"" + duplicateStationName + "\" already exists"
                    )
                );
            }
        }

        return Optional.empty();
    }
}
