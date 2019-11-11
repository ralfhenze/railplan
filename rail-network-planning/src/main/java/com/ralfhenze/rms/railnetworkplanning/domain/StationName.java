package com.ralfhenze.rms.railnetworkplanning.domain;

/**
 * [ ] a Station's Name begins with an uppercase letter
 * [ ] a Station's Name contains [a-zA-Z\ \.\-\(\)]
 *     -> Smart Constructor
 */
class StationName {

    private final String name;

    StationName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
