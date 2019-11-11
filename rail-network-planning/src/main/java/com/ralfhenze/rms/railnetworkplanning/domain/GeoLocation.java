package com.ralfhenze.rms.railnetworkplanning.domain;

/**
 * [x] a Station's GeoLocation always has Latitude and Longitude
 *     -> Smart Constructor
 *
 * https://github.com/locationtech/spatial4j
 * https://github.com/JavadocMD/simplelatlng
 */
class GeoLocation {
    double latitude;
    double longitude;

    // https://introcs.cs.princeton.edu/java/44st/Location.java.html
    double distanceTo(GeoLocation location) {
        return 1.0;
    }
}
