package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.ValueObject;

/**
 * [x] a Station's GeoLocation always has Latitude and Longitude
 *     -> Smart Constructor
 *
 * https://github.com/locationtech/spatial4j
 * https://github.com/JavadocMD/simplelatlng
 */
class GeoLocation implements ValueObject {
    private final double latitude;
    private final double longitude;

    GeoLocation(final double latitude, final double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // https://introcs.cs.princeton.edu/java/44st/Location.java.html
    double distanceTo(final GeoLocation location) {
        return 1.0;
    }
}
