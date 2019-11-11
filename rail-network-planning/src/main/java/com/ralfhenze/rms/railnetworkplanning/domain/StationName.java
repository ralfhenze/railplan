package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.ValueObject;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.assertNotBlank;

/**
 * [ ] a Station's Name begins with an uppercase letter
 * [ ] a Station's Name contains [a-zA-Z\ \.\-\(\)]
 *     -> Smart Constructor
 */
class StationName implements ValueObject {

    private final String name;

    StationName(final String name) {
        assertNotBlank(name, "Name must not be blank");
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
