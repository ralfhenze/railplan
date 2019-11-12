package com.ralfhenze.rms.railnetworkplanning.domain.station;

import com.ralfhenze.rms.railnetworkplanning.domain.common.ValueObject;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

/**
 * [-] a Station is located on land
 *     -> since this is quite complicated I will pretend it for now
 * [ ] a Station is located within Germany's bounding rectangle
 *     -> Smart Constructor
 */
public class GeoLocationInGermany implements ValueObject {

    private final GeoLocation location;

    public GeoLocationInGermany(final GeoLocation location) {
        this.location = ensureNotNull(location, "Geo location is required");
    }
}
