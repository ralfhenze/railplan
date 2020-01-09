package com.ralfhenze.railplan.domain.railnetwork.elements;

import com.ralfhenze.railplan.domain.common.ValueObject;
import com.ralfhenze.railplan.domain.common.validation.Field;
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

    private final static int MIN_LENGTH = 4;
    private final static String VALID_NAME_REGEX = "^[A-ZÄÖÜ]{1}[a-zäöüßA-Z\\ \\.\\-\\(\\)]{3,29}$";

    private final String name;

    /**
     * @throws ValidationException if name violates Station name invariants
     */
    public TrainStationName(final String name) {
        new Validation()
            .ensureThat(name, new HasMinLength(MIN_LENGTH), Field.STATION_NAME)
            .ensureThat(name, new MatchesRegex(VALID_NAME_REGEX), Field.STATION_NAME)
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
