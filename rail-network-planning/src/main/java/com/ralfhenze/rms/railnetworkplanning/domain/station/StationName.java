package com.ralfhenze.rms.railnetworkplanning.domain.station;

import com.ralfhenze.rms.railnetworkplanning.domain.common.ValueObject;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotBlank;

/**
 * [ ] a Station's Name begins with an uppercase letter
 * [ ] a Station's Name contains [a-zA-Z\ \.\-\(\)]
 *     -> Smart Constructor
 */
public class StationName implements ValueObject {

    private final String name;

    public StationName(final String name) {
        this.name = ensureNotBlank(name, "Name must not be blank");
    }

    public String getName() {
        return name;
    }
}
