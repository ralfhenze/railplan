package com.ralfhenze.rms.railnetworkplanning.domain.station;

import com.ralfhenze.rms.railnetworkplanning.domain.common.ValueObject;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureWithinRange;

/**
 * [x] a Station's GeoLocation always has Latitude and Longitude
 *     -> Smart Constructor
 *
 * https://github.com/locationtech/spatial4j
 * https://github.com/JavadocMD/simplelatlng
 *
 * https://en.wikipedia.org/wiki/Geographic_coordinate_system
 * https://en.wikipedia.org/wiki/Latitude
 * https://en.wikipedia.org/wiki/Longitude
 */
public class GeoLocation implements ValueObject {

    private final double latitude;
    private final double longitude;

    public GeoLocation(final double latitude, final double longitude) {
        this.latitude = ensureWithinRange(latitude, -90.0, 90.0, "Latitude");
        this.longitude = ensureWithinRange(longitude, -180.0, 180.0, "Longitude");
    }

    // TODO: implement
    // https://introcs.cs.princeton.edu/java/44st/Location.java.html
    double distanceTo(final GeoLocation location) {
        return 1.0;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
