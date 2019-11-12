package com.ralfhenze.rms.railnetworkplanning.domain.station;

import com.ralfhenze.rms.railnetworkplanning.domain.common.ValueObject;

/**
 * [x] a Station's GeoLocation always has Latitude and Longitude
 *     -> Smart Constructor
 *
 * https://github.com/locationtech/spatial4j
 * https://github.com/JavadocMD/simplelatlng
 */
public class GeoLocation implements ValueObject {

    private final double latitude;
    private final double longitude;

    public GeoLocation(final double latitude, final double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // https://introcs.cs.princeton.edu/java/44st/Location.java.html
    double distanceTo(final GeoLocation location) {
        return 1.0;
    }
}
