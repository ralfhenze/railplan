package com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements;

import com.ralfhenze.rms.railnetworkplanning.domain.common.ValueObject;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureWithinRange;

/**
 * [x] a Station's GeoLocation always has Latitude and Longitude
 * [x] Latitude is within [-90°, 90°] and Longitude within [-180°, 180°]
 *
 * https://en.wikipedia.org/wiki/Geographic_coordinate_system
 * https://en.wikipedia.org/wiki/Latitude
 * https://en.wikipedia.org/wiki/Longitude
 */
public class GeoLocation implements ValueObject {

    private final double EARTH_RADIUS_IN_KILOMETERS = 6371.007177356707;

    private final double latitude;
    private final double longitude;

    public GeoLocation(final double latitude, final double longitude) {
        this.latitude = ensureWithinRange(latitude, -90.0, 90.0, "Latitude");
        this.longitude = ensureWithinRange(longitude, -180.0, 180.0, "Longitude");
    }

    /**
     * https://en.wikipedia.org/wiki/Haversine_formula
     * https://stackoverflow.com/questions/120283/how-can-i-measure-distance-and-create-a-bounding-box-based-on-two-latitudelongi/57346001
     */
    public double getKilometerDistanceTo(final GeoLocation location) {
        double lat1 = Math.toRadians(latitude);
        double lng1 = Math.toRadians(longitude);
        double lat2 = Math.toRadians(location.getLatitude());
        double lng2 = Math.toRadians(location.getLongitude());

        double latDiff = lat2 - lat1;
        double lngDiff = lng2 - lng1;

        double a = Math.pow((Math.sin(latDiff / 2)), 2)
            + Math.cos(lat1)
            * Math.cos(lat2)
            * Math.pow(Math.sin(lngDiff / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_IN_KILOMETERS * c;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
