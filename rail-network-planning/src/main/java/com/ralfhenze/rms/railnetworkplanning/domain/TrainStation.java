package com.ralfhenze.rms.railnetworkplanning.domain;

/**
 * https://en.wikipedia.org/wiki/Train_station
 *
 * Local Entity within RailNetwork Aggregate
 */
class TrainStation {
    StationId id;
    StationName name;
    GeoLocationInGermany location;
}
