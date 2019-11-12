package com.ralfhenze.rms.railnetworkplanning.domain.station;

import com.ralfhenze.rms.railnetworkplanning.domain.common.ValueObject;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;
import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureWithinRange;

/**
 * [-] a Station is located on land
 *     -> since this is quite complicated I will pretend it for now
 * [x] a Station is located within Germany's bounding rectangle
 */
public class GeoLocationInGermany implements ValueObject {

    // Germany bounding box taken from
    // https://gist.github.com/graydon/11198540
    private final static double GERMANY_EAST_LAT = 55.099161;
    private final static double GERMANY_WEST_LAT = 47.2701114;
    private final static double GERMANY_NORTH_LNG = 15.0419319;
    private final static double GERMANY_SOUTH_LNG = 5.8663153;

    private final GeoLocation location;

    public GeoLocationInGermany(final GeoLocation location) {
        ensureNotNull(location, "Geo Location");
        ensureWithinRange(location.getLatitude(), GERMANY_WEST_LAT, GERMANY_EAST_LAT, "Latitude");
        ensureWithinRange(location.getLongitude(), GERMANY_SOUTH_LNG, GERMANY_NORTH_LNG, "Longitude");

        this.location = location;
    }

    public GeoLocation getLocation() {
        return location;
    }
}
