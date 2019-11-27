package com.ralfhenze.railplan.domain.railnetwork.elements;

import com.ralfhenze.railplan.domain.common.ValueObject;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureWithinRange;

/**
 * [-] a Station is located on land
 *     -> since this is quite complicated I will pretend it for now
 * [x] a Station is located within Germany's bounding rectangle
 *
 * https://en.wikipedia.org/wiki/Geographic_coordinate_system
 * https://en.wikipedia.org/wiki/Latitude
 * https://en.wikipedia.org/wiki/Longitude
 */
public class GeoLocationInGermany implements ValueObject {

    // Germany bounding box taken from
    // https://gist.github.com/graydon/11198540
    private final static double GERMANY_EAST_LAT = 55.099161;
    private final static double GERMANY_WEST_LAT = 47.2701114;
    private final static double GERMANY_NORTH_LNG = 15.0419319;
    private final static double GERMANY_SOUTH_LNG = 5.8663153;

    private final static double EARTH_RADIUS_IN_KILOMETERS = 6371.007177356707;

    private final double latitude;
    private final double longitude;

    public GeoLocationInGermany(final double latitude, final double longitude) {
        this.latitude = ensureWithinRange(latitude, GERMANY_WEST_LAT, GERMANY_EAST_LAT, "Latitude");
        this.longitude = ensureWithinRange(longitude, GERMANY_SOUTH_LNG, GERMANY_NORTH_LNG, "Longitude");
    }

    /**
     * https://en.wikipedia.org/wiki/Haversine_formula
     * https://stackoverflow.com/questions/120283/how-can-i-measure-distance-and-create-a-bounding-box-based-on-two-latitudelongi/57346001
     */
    public double getKilometerDistanceTo(final GeoLocationInGermany location) {
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
