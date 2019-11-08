package com.ralfhenze.rms.railnetworkplanning.domain;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

class StationId {}

/**
 * [x] a Station's Name begins with an uppercase letter
 * [x] a Station's Name contains [a-zA-Z\ \.\-\(\)]
 *     -> Smart Constructor
 */
class StationName {
    String name;
}

/**
 * [x] a Station's GeoLocation always has Latitude and Longitude
 *     -> Smart Constructor
 *
 * https://github.com/locationtech/spatial4j
 * https://github.com/JavadocMD/simplelatlng
 */
abstract class GeoLocation {
    double latitude;
    double longitude;

    // https://introcs.cs.princeton.edu/java/44st/Location.java.html
    abstract double distanceTo(GeoLocation location);
}

/**
 * [-] a Station is located on land
 *     -> since this is quite complicated I will pretend it for now
 * [x] a Station is located within Germany's bounding rectangle
 *     -> getContainedLocation() will assert that
 *
 * or Smart Constructor in GeoLocationInGermany
 */
abstract class Germany {
    abstract Optional<GeoLocationInGermany> getContainedLocation(GeoLocation location);
}

/**
 * Needs a private constructor, only accessible for Germany!
 */
class GeoLocationInGermany {
    GeoLocation location;
}

/**
 * https://en.wikipedia.org/wiki/Train_station
 *
 * Braucht der immer eine Id?
 */
abstract class TrainStation {
    StationId id;
    StationName name;
    GeoLocationInGermany location;
}

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

/**
 * MODIFIABLE
 *
 * [x] the maximum length of a Track is 200 km
 * [x] the minimum distance between two Stations is 10 km
 *     -> addStation(), moveStation()
 * [x] a Station's Name is unique
 *     -> addStation(), renameStation()
 * [x] two Stations can only be connected by a single Track
 *     -> connectStations()
 */
abstract class WorkInProgressRailNetwork {
    Set<TrainStation> stations;
    Set<DoubleTrackRailway> connections;

    abstract StationId addStation(TrainStation station);
    // maybe make immutable and return new WorkInProgressRailNetwork instead of void
    abstract void renameStation(StationId id, StationName name);
    abstract void moveStation(StationId id, GeoLocationInGermany location);
    abstract void deleteStation(StationId id);
    abstract void connectStations(StationId id1, StationId id2);
    abstract void disconnectStations(StationId id1, StationId id2);

    abstract Optional<ValidatedRailNetwork> validate();
}

class NonEmptySet<T> {}
class SetWithAtLeastTwoElements<T> {}

/**
 * READ-ONLY
 *
 * [x] the Rail Network Plan contains at least two Stations and one Track
 * [x] the Rail Network Plan contains no stand-alone / unconnected Stations
 * [x] the Rail Network Plan is a single graph without unconnected islands / sub-graphs
 *     -> Smart Constructor with WorkInProgressRailNetwork as input
 *        (otherwise I would need to ensure the same invariants again)
 *        (-) would require a "new ValidatedRailNetwork(this)" in WorkInProgressRailNetwork.validate()
 *        (-) circular dependency
 *     -> or dedicated Validation Service
 */
abstract class ValidatedRailNetwork {
    SetWithAtLeastTwoElements<TrainStation> stations;
    NonEmptySet<DoubleTrackRailway> connections;

    // or dedicated Release Service
    // (-) wouldn't work at the beginning, when there is nothing released yet
    abstract Optional<ReleasedRailNetwork> releaseAfter(ReleasedRailNetwork releasedNetwork, LocalDate untilDate);

    abstract WorkInProgressRailNetwork makeModifiable();
}

class RailNetworkId {}

/**
 * [x] the TimePeriod's minimum duration is 6 months
 * [x] the TimePeriod's StartDate is before (<) EndDate
 *     -> Smart Constructor
 */
class RailNetworkPeriod {}

/**
 * READ-ONLY
 *
 * [x] released Rail Network Plans can't be changed any more
 *     -> immutable object, no setters
 */
abstract class ReleasedRailNetwork {
    RailNetworkId id;
    RailNetworkPeriod period;
    SetWithAtLeastTwoElements<TrainStation> stations;
    NonEmptySet<DoubleTrackRailway> connections;
}

/**
 * [x] the Periods of released Rail Network Plans are continuous without gaps and don't overlap
 *     -> release()
 *
 * Needs to publish RailNetworkReleased event
 */
abstract class RailNetworkReleaseService {
    abstract Optional<ReleasedRailNetwork> release(ValidatedRailNetwork network, RailNetworkPeriod period);
}
