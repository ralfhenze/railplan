package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.station.GeoLocation;
import com.ralfhenze.rms.railnetworkplanning.domain.station.GeoLocationInGermany;
import com.ralfhenze.rms.railnetworkplanning.domain.station.StationId;
import com.ralfhenze.rms.railnetworkplanning.domain.station.StationName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RailNetworkDraftTest {

    private final StationName berlinHbf = new StationName("Berlin Hbf");
    private final GeoLocationInGermany berlinHbfPos = new GeoLocationInGermany(new GeoLocation(52.524927, 13.369348));

    private final StationName berlinOst = new StationName("Berlin Ostbahnhof");
    private final GeoLocationInGermany berlinOstPos = new GeoLocationInGermany(new GeoLocation(52.510784, 13.434832));

    private final StationName potsdamHbf = new StationName("Potsdam Hbf");
    private final GeoLocationInGermany potsdamHbfPos = new GeoLocationInGermany(new GeoLocation(52.391726, 13.067120));

    private final StationName stuttgartHbf = new StationName("Stuttgart Hbf");
    private final GeoLocationInGermany stuttgartHbfPos = new GeoLocationInGermany(new GeoLocation(48.784245, 9.182160));

    @Test
    void should_ensure_unique_station_names() {
        RailNetworkDraft draft = new RailNetworkDraft(new RailNetworkDraftId("1"));
        draft.addStation(berlinHbf, berlinHbfPos);
        StationId potsdamHbfId = draft.addStation(potsdamHbf, potsdamHbfPos);

        assertThrows(IllegalArgumentException.class, () -> {
            draft.addStation(berlinHbf, stuttgartHbfPos);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            draft.renameStation(potsdamHbfId, berlinHbf);
        });
    }

    @Test
    void should_ensure_minimum_distance_between_stations() {
        RailNetworkDraft draft = new RailNetworkDraft(new RailNetworkDraftId("1"));
        draft.addStation(berlinHbf, berlinHbfPos);
        StationId potsdamHbfId = draft.addStation(potsdamHbf, potsdamHbfPos);

        assertThrows(IllegalArgumentException.class, () -> {
            draft.addStation(berlinOst, berlinOstPos);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            draft.moveStation(potsdamHbfId, berlinOstPos);
        });
    }

    @Test
    void should_ensure_no_duplicate_connections() {
        RailNetworkDraft draft = new RailNetworkDraft(new RailNetworkDraftId("1"));
        StationId berlinHbfId = draft.addStation(berlinHbf, berlinHbfPos);
        StationId potsdamHbfId = draft.addStation(potsdamHbf, potsdamHbfPos);
        draft.connectStations(berlinHbfId, potsdamHbfId);

        assertThrows(IllegalArgumentException.class, () -> {
            draft.connectStations(potsdamHbfId, berlinHbfId);
        });
    }

    @Test
    void should_ensure_max_track_length() {
        RailNetworkDraft draft = new RailNetworkDraft(new RailNetworkDraftId("1"));
        StationId berlinHbfId = draft.addStation(berlinHbf, berlinHbfPos);
        StationId stuttgartHbfId = draft.addStation(stuttgartHbf, stuttgartHbfPos);

        assertThrows(IllegalArgumentException.class, () -> {
            draft.connectStations(berlinHbfId, stuttgartHbfId);
        });
    }

    @Test
    void should_be_able_to_delete_stations() {
        RailNetworkDraft draft = new RailNetworkDraft(new RailNetworkDraftId("1"));
        StationId berlinHbfId = draft.addStation(berlinHbf, berlinHbfPos);
        assertEquals(1, draft.getStations().size());

        draft.deleteStation(berlinHbfId);

        assertTrue(draft.getStations().isEmpty());
    }

    @Test
    void should_be_able_to_delete_connections() {
        RailNetworkDraft draft = new RailNetworkDraft(new RailNetworkDraftId("1"));
        StationId berlinHbfId = draft.addStation(berlinHbf, berlinHbfPos);
        StationId potsdamHbfId = draft.addStation(potsdamHbf, potsdamHbfPos);
        draft.connectStations(berlinHbfId, potsdamHbfId);

        assertEquals(1, draft.getConnections().size());

        draft.disconnectStations(potsdamHbfId, berlinHbfId);

        assertTrue(draft.getConnections().isEmpty());
    }

    @Test
    void should_delete_associated_connections_on_station_delete() {
        RailNetworkDraft draft = new RailNetworkDraft(new RailNetworkDraftId("1"));
        StationId berlinHbfId = draft.addStation(berlinHbf, berlinHbfPos);
        StationId potsdamHbfId = draft.addStation(potsdamHbf, potsdamHbfPos);
        draft.connectStations(berlinHbfId, potsdamHbfId);

        assertEquals(2, draft.getStations().size());
        assertEquals(1, draft.getConnections().size());

        draft.deleteStation(potsdamHbfId);

        assertEquals(1, draft.getStations().size());
        assertTrue(draft.getConnections().isEmpty());
    }
}
