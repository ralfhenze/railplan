package com.ralfhenze.rms.railnetworkplanning.domain;

/**
 * https://en.wikipedia.org/wiki/Double-track_railway
 *
 * [x] a Track connects two different Stations
 *     -> Smart Constructor
 * [x] a Track has no direction
 *     -> implementation
 *
 * equal when two DoubleTrackRailways connect the same Stations
 */
class DoubleTrackRailway {
    StationId station1;
    StationId station2;
}
