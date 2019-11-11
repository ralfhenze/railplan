package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.ValueObject;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.assertNotNull;

/**
 * [-] a Station is located on land
 *     -> since this is quite complicated I will pretend it for now
 * [ ] a Station is located within Germany's bounding rectangle
 *     -> Smart Constructor
 */
class GeoLocationInGermany implements ValueObject {

    private final GeoLocation location;

    GeoLocationInGermany(final GeoLocation location) {
        assertNotNull(location, "Geo location is required");

        this.location = location;
    }
}
