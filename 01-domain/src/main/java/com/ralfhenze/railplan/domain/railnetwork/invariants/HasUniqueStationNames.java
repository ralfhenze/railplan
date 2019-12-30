package com.ralfhenze.railplan.domain.railnetwork.invariants;

import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.Collection;
import java.util.Optional;

public class HasUniqueStationNames implements ValidationConstraint<ImmutableList<TrainStation>> {

    @Override
    public Optional<ValidationError> validate(final ImmutableList<TrainStation> stations) {
        if (stations.size() >= 2) {
            final var duplicateStationName = stations
                .groupBy(TrainStation::getName)
                .selectKeysMultiValues((name, s) -> ((Collection) s).size() > 1)
                .keyBag()
                .getAny();

            if (duplicateStationName != null) {
                return Optional.of(
                    new ValidationError(
                        "Station Name \"" + duplicateStationName + "\" already exists"
                    )
                );
            }
        }

        return Optional.empty();
    }
}
