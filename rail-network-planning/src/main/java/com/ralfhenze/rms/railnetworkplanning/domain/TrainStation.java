package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.LocalEntity;

/**
 * https://en.wikipedia.org/wiki/Train_station
 *
 * Local Entity within RailNetwork Aggregate
 */
class TrainStation implements LocalEntity {
    StationId id;
    StationName name;
    GeoLocationInGermany location;
}
