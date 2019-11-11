package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Aggregate;

/**
 * READ-ONLY
 *
 * [x] released Rail Network Plans can't be changed any more
 *     -> immutable object, no setters
 */
class ReleasedRailNetwork implements Aggregate {
    RailNetworkId id;
    RailNetworkPeriod period;
    SetWithAtLeastTwoElements<TrainStation> stations;
    NonEmptySet<DoubleTrackRailway> connections;
}
