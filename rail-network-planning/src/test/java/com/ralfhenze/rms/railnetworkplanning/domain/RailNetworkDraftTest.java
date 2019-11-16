package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.station.StationId;
import org.junit.jupiter.api.Test;

import static com.ralfhenze.rms.railnetworkplanning.domain.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

class RailNetworkDraftTest {

    @Test
    void should_ensure_unique_station_names() {
        RailNetworkDraft draft = new RailNetworkDraft(new RailNetworkDraftId("1"));
        draft.addStation(berlinHbfName, berlinHbfPos);
        StationId potsdamHbfId = draft.addStation(potsdamHbfName, potsdamHbfPos);

        assertThrows(IllegalArgumentException.class, () -> {
            draft.addStation(berlinHbfName, stuttgartHbfPos);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            draft.renameStation(potsdamHbfId, berlinHbfName);
        });
    }

    @Test
    void should_ensure_minimum_distance_between_stations() {
        RailNetworkDraft draft = new RailNetworkDraft(new RailNetworkDraftId("1"));
        draft.addStation(berlinHbfName, berlinHbfPos);
        StationId potsdamHbfId = draft.addStation(potsdamHbfName, potsdamHbfPos);

        assertThrows(IllegalArgumentException.class, () -> {
            draft.addStation(berlinOstName, berlinOstPos);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            draft.moveStation(potsdamHbfId, berlinOstPos);
        });
    }

    @Test
    void should_ensure_no_duplicate_connections() {
        RailNetworkDraft draft = new RailNetworkDraft(new RailNetworkDraftId("1"));
        StationId berlinHbfId = draft.addStation(berlinHbfName, berlinHbfPos);
        StationId potsdamHbfId = draft.addStation(potsdamHbfName, potsdamHbfPos);
        draft.connectStations(berlinHbfId, potsdamHbfId);

        assertThrows(IllegalArgumentException.class, () -> {
            draft.connectStations(potsdamHbfId, berlinHbfId);
        });
    }

    @Test
    void should_ensure_max_track_length() {
        RailNetworkDraft draft = new RailNetworkDraft(new RailNetworkDraftId("1"));
        StationId berlinHbfId = draft.addStation(berlinHbfName, berlinHbfPos);
        StationId stuttgartHbfId = draft.addStation(stuttgartHbfName, stuttgartHbfPos);

        assertThrows(IllegalArgumentException.class, () -> {
            draft.connectStations(berlinHbfId, stuttgartHbfId);
        });
    }

    @Test
    void should_be_able_to_delete_stations() {
        RailNetworkDraft draft = new RailNetworkDraft(new RailNetworkDraftId("1"));
        StationId berlinHbfId = draft.addStation(berlinHbfName, berlinHbfPos);
        assertEquals(1, draft.getStations().size());

        draft.deleteStation(berlinHbfId);

        assertTrue(draft.getStations().isEmpty());
    }

    @Test
    void should_be_able_to_delete_connections() {
        RailNetworkDraft draft = new RailNetworkDraft(new RailNetworkDraftId("1"));
        StationId berlinHbfId = draft.addStation(berlinHbfName, berlinHbfPos);
        StationId potsdamHbfId = draft.addStation(potsdamHbfName, potsdamHbfPos);
        draft.connectStations(berlinHbfId, potsdamHbfId);

        assertEquals(1, draft.getConnections().size());

        draft.disconnectStations(potsdamHbfId, berlinHbfId);

        assertTrue(draft.getConnections().isEmpty());
    }

    @Test
    void should_delete_associated_connections_on_station_delete() {
        RailNetworkDraft draft = new RailNetworkDraft(new RailNetworkDraftId("1"));
        StationId berlinHbfId = draft.addStation(berlinHbfName, berlinHbfPos);
        StationId potsdamHbfId = draft.addStation(potsdamHbfName, potsdamHbfPos);
        draft.connectStations(berlinHbfId, potsdamHbfId);

        assertEquals(2, draft.getStations().size());
        assertEquals(1, draft.getConnections().size());

        draft.deleteStation(potsdamHbfId);

        assertEquals(1, draft.getStations().size());
        assertTrue(draft.getConnections().isEmpty());
    }
}
