package com.ralfhenze.rms.railnetworkplanning.domain;

import java.util.Optional;

/**
 * [-] a Station is located on land
 *     -> since this is quite complicated I will pretend it for now
 * [x] a Station is located within Germany's bounding rectangle
 *     -> getContainedLocation() will assert that
 *
 * or Smart Constructor in GeoLocationInGermany
 */
class Germany {

    Optional<GeoLocationInGermany> getContainedLocation(GeoLocation location) {
        return Optional.empty();
    }
}
