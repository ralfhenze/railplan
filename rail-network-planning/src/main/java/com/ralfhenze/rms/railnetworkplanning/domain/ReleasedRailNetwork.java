package com.ralfhenze.rms.railnetworkplanning.domain;

/**
 * READ-ONLY
 *
 * [x] released Rail Network Plans can't be changed any more
 *     -> immutable object, no setters
 */
class ReleasedRailNetwork {
    RailNetworkId id;
    RailNetworkPeriod period;
    SetWithAtLeastTwoElements<TrainStation> stations;
    NonEmptySet<DoubleTrackRailway> connections;
}
