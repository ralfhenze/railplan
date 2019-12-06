package com.ralfhenze.railplan.domain.railnetwork.elements;

import com.ralfhenze.railplan.domain.common.ValueObject;
import com.ralfhenze.railplan.domain.common.validation.Validation;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.common.validation.constraints.HasMinLength;
import com.ralfhenze.railplan.domain.common.validation.constraints.MatchesRegex;

/**
 * [x] a Station's Name begins with an uppercase letter
 * [x] a Station's Name contains [a-zA-Z\ \.\-\(\)]
 * [x] a Station's Name has a min length of 4 characters
 * [x] a Station's Name has a max length of 30 characters
 */
public class TrainStationName implements ValueObject {

    private final static String VALID_NAME_REGEX = "^[A-ZÄÖÜ]{1}[a-zäöüßA-Z\\ \\.\\-\\(\\)]{3,29}$";

    private final String name;

    public TrainStationName(final String name) throws ValidationException {
        new Validation()
            .ensureThat(name, new MatchesRegex(VALID_NAME_REGEX), "Station name")
            .ensureThat(name, new HasMinLength(4), "Station name")
            .throwExceptionIfInvalid();

        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return (o != null && ((TrainStationName)o).name.equals(this.name));
    }

    @Override
    public String toString() {
        return name;
    }
}
