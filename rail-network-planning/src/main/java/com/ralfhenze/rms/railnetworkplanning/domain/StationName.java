package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.ValueObject;

/**
 * [ ] a Station's Name begins with an uppercase letter
 * [ ] a Station's Name contains [a-zA-Z\ \.\-\(\)]
 *     -> Smart Constructor
 */
class StationName implements ValueObject {

    private final String name;

    StationName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
