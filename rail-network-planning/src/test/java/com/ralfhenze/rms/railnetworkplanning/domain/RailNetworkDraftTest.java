package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.station.GeoLocation;
import com.ralfhenze.rms.railnetworkplanning.domain.station.GeoLocationInGermany;
import com.ralfhenze.rms.railnetworkplanning.domain.station.StationId;
import com.ralfhenze.rms.railnetworkplanning.domain.station.StationName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RailNetworkDraftTest {

    @Test
    void should_ensure_unique_station_names() {
        RailNetworkDraft draft = new RailNetworkDraft(new RailNetworkDraftId("1"));
        GeoLocationInGermany location1 = new GeoLocationInGermany(new GeoLocation(51.0, 11.0));
        GeoLocationInGermany location2 = new GeoLocationInGermany(new GeoLocation(52.0, 12.0));
        GeoLocationInGermany location3 = new GeoLocationInGermany(new GeoLocation(53.0, 13.0));
        StationId firstStationId = draft.addStation(new StationName("First Station"), location1);
        StationId secondStationId = draft.addStation(new StationName("Second Station"), location2);

        assertThrows(Exception.class, () -> {
            draft.addStation(new StationName("First Station"), location3);
        });

        assertThrows(Exception.class, () -> {
            draft.renameStation(secondStationId, new StationName("First Station"));
        });
    }

    @Test
    void should_ensure_minimum_distance_between_stations() {
        RailNetworkDraft draft = new RailNetworkDraft(new RailNetworkDraftId("1"));
        StationId berlinHbfId = draft.addStation(
            new StationName("Berlin Hbf"),
            new GeoLocationInGermany(new GeoLocation(52.524927, 13.369348))
        );
        StationId potsdamHbfId = draft.addStation(
            new StationName("Potsdam Hbf"),
            new GeoLocationInGermany(new GeoLocation(52.391726, 13.067120))
        );

        assertThrows(Exception.class, () -> {
            draft.addStation(
                new StationName("Berlin Ostbahnhof"), // is nearer than 10 km
                new GeoLocationInGermany(new GeoLocation(52.510784, 13.434832))
            );
        });

        assertThrows(Exception.class, () -> {
            draft.moveStation(
                potsdamHbfId,
                new GeoLocationInGermany(new GeoLocation(52.510784, 13.434832))
            );
        });
    }

    @Test
    void should_ensure_no_duplicate_connections() {
        RailNetworkDraft draft = new RailNetworkDraft(new RailNetworkDraftId("1"));
        StationId berlinHbfId = draft.addStation(
            new StationName("Berlin Hbf"),
            new GeoLocationInGermany(new GeoLocation(52.524927, 13.369348))
        );
        StationId potsdamHbfId = draft.addStation(
            new StationName("Potsdam Hbf"),
            new GeoLocationInGermany(new GeoLocation(52.391726, 13.067120))
        );
        draft.connectStations(berlinHbfId, potsdamHbfId);

        assertThrows(Exception.class, () -> {
            draft.connectStations(potsdamHbfId, berlinHbfId);
        });
    }

    @Test
    void should_ensure_max_track_length() {
        RailNetworkDraft draft = new RailNetworkDraft(new RailNetworkDraftId("1"));
        StationId berlinHbfId = draft.addStation(
            new StationName("Berlin Hbf"),
            new GeoLocationInGermany(new GeoLocation(52.524927, 13.369348))
        );
        StationId stuttgartHbfId = draft.addStation(
            new StationName("Stuttgart Hbf"),
            new GeoLocationInGermany(new GeoLocation(48.784245, 9.182160))
        );

        assertThrows(Exception.class, () -> {
            draft.connectStations(berlinHbfId, stuttgartHbfId);
        });
    }
}
