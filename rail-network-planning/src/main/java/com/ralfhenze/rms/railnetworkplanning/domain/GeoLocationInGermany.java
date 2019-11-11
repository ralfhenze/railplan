package com.ralfhenze.rms.railnetworkplanning.domain;

/**
 * [-] a Station is located on land
 *     -> since this is quite complicated I will pretend it for now
 * [ ] a Station is located within Germany's bounding rectangle
 *     -> Smart Constructor
 */
class GeoLocationInGermany {
    private final GeoLocation location;

    GeoLocationInGermany(final GeoLocation location) {
        this.location = location;
    }
}
