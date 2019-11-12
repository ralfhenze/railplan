package com.ralfhenze.rms.railnetworkplanning.domain.station;

import com.ralfhenze.rms.railnetworkplanning.domain.common.ValueObject;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotBlank;
import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureRegexMatch;

/**
 * [x] a Station's Name begins with an uppercase letter
 * [x] a Station's Name contains [a-zA-Z\ \.\-\(\)]
 * [x] a Station's Name has a min length of 4 characters
 * [x] a Station's Name has a max length of 30 characters
 */
public class StationName implements ValueObject {

    private final String name;

    public StationName(final String name) {
        ensureNotBlank(name, "Station name must not be blank"); // TODO: <- not needed any more
        ensureRegexMatch(name, "^[A-ZÄÖÜ]{1}[a-zäöüßA-Z\\ \\.\\-\\(\\)]{3,29}$", "Station name");

        this.name = name;
    }

    public String getName() {
        return name;
    }
}
