package com.ralfhenze.railplan.domain.railnetwork.invariants;

import com.ralfhenze.railplan.domain.common.validation.Field;
import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.Optional;

public class DoesNotAlreadyExist implements ValidationConstraint<String> {

    private final ImmutableList<String> otherStationNames;

    public DoesNotAlreadyExist(final ImmutableList<String> otherStationNames) {
        this.otherStationNames = otherStationNames;
    }

    @Override
    public Optional<ValidationError> validate(
        final String stationName,
        final Field field
    ) {
        if (otherStationNames.contains(stationName)) {
            return Optional.of(
                new ValidationError(
                    "Station Name \"" + stationName + "\" already exists", field
                )
            );
        }

        return Optional.empty();
    }
}
