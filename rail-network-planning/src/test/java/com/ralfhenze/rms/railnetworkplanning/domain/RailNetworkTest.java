package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.station.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RailNetworkTest {

    private final TrainStation berlinHbf = new TrainStation(
        new StationId("1"),
        new StationName("Berlin Hbf"),
        new GeoLocationInGermany(new GeoLocation(52.524927, 13.369348))
    );
    private final TrainStation hamburgHbf = new TrainStation(
        new StationId("2"),
        new StationName("Hamburg Hbf"),
        new GeoLocationInGermany(new GeoLocation(53.552596, 10.006727))
    );
    private final TrainStation frankfurtHbf = new TrainStation(
        new StationId("3"),
        new StationName("Frankfurt am Main Hbf"),
        new GeoLocationInGermany(new GeoLocation(50.106880, 8.663739))
    );
    private final TrainStation stuttgartHbf = new TrainStation(
        new StationId("4"),
        new StationName("Stuttgart Hbf"),
        new GeoLocationInGermany(new GeoLocation(48.784245, 9.182160))
    );
    private final TrainStation berlinOstbahnhof = new TrainStation(
        new StationId("5"),
        new StationName("Berlin Ostbahnhof"),
        new GeoLocationInGermany(new GeoLocation(52.510784, 13.434832))
    );


    @Test
    void should_ensure_at_least_two_stations() {
        assertThrows(Exception.class, () -> {
            new RailNetwork(
                new RailNetworkId("1"),
                new RailNetworkPeriod(LocalDate.of(2019, 11, 14), LocalDate.of(2019, 11, 20)),
                new HashSet<>(),
                new HashSet<>()
            );
        });
    }

    @Test
    void should_ensure_at_least_one_connection() {
        assertThrows(Exception.class, () -> {
            new RailNetwork(
                new RailNetworkId("1"),
                new RailNetworkPeriod(LocalDate.of(2019, 11, 14), LocalDate.of(2019, 11, 20)),
                new HashSet<>(Arrays.asList(berlinHbf, hamburgHbf)),
                new HashSet<>()
            );
        });
    }

    @Test
    void should_ensure_no_unconnected_sub_graphs() {
        assertThrows(Exception.class, () -> {
            new RailNetwork(
                new RailNetworkId("1"),
                new RailNetworkPeriod(LocalDate.of(2019, 11, 14), LocalDate.of(2019, 11, 20)),
                new HashSet<>(Arrays.asList(berlinHbf, hamburgHbf, frankfurtHbf, stuttgartHbf)),
                new HashSet<>(Arrays.asList(
                    new DoubleTrackRailway(berlinHbf.getId(), hamburgHbf.getId()),
                    new DoubleTrackRailway(frankfurtHbf.getId(), stuttgartHbf.getId())
                ))
            );
        });
    }

    @Test
    void should_ensure_max_track_length() {
        assertThrows(Exception.class, () -> {
            new RailNetwork(
                new RailNetworkId("1"),
                new RailNetworkPeriod(LocalDate.of(2019, 11, 14), LocalDate.of(2019, 11, 20)),
                new HashSet<>(Arrays.asList(berlinHbf, stuttgartHbf)),
                new HashSet<>(Arrays.asList(
                    new DoubleTrackRailway(berlinHbf.getId(), stuttgartHbf.getId())
                ))
            );
        });
    }

    @Test
    void should_ensure_unique_station_names() {
        assertThrows(Exception.class, () -> {
            new RailNetwork(
                new RailNetworkId("1"),
                new RailNetworkPeriod(LocalDate.of(2019, 11, 14), LocalDate.of(2019, 11, 20)),
                new HashSet<>(Arrays.asList(berlinHbf, hamburgHbf.withName(new StationName("Berlin Hbf")))),
                new HashSet<>(Arrays.asList(
                    new DoubleTrackRailway(berlinHbf.getId(), hamburgHbf.getId())
                ))
            );
        });
    }

    @Test
    void should_ensure_minimum_station_distance() {
        assertThrows(Exception.class, () -> {
            new RailNetwork(
                new RailNetworkId("1"),
                new RailNetworkPeriod(LocalDate.of(2019, 11, 14), LocalDate.of(2019, 11, 20)),
                new LinkedHashSet<>(Arrays.asList(berlinHbf, berlinOstbahnhof, hamburgHbf)),
                new LinkedHashSet<>(Arrays.asList(
                    new DoubleTrackRailway(berlinHbf.getId(), berlinOstbahnhof.getId()),
                    new DoubleTrackRailway(berlinHbf.getId(), hamburgHbf.getId())
                ))
            );
        });
    }

    @Test
    void should_ensure_no_duplicate_connections() {
        assertThrows(Exception.class, () -> {
            new RailNetwork(
                new RailNetworkId("1"),
                new RailNetworkPeriod(LocalDate.of(2019, 11, 14), LocalDate.of(2019, 11, 20)),
                new LinkedHashSet<>(Arrays.asList(berlinHbf, hamburgHbf)),
                new LinkedHashSet<>(Arrays.asList(
                    new DoubleTrackRailway(berlinHbf.getId(), hamburgHbf.getId()),
                    new DoubleTrackRailway(hamburgHbf.getId(), berlinHbf.getId())
                ))
            );
        });
    }
}
