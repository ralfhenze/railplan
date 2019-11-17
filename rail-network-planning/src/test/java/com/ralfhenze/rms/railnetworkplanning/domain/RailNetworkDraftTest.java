package com.ralfhenze.rms.railnetworkplanning.domain;

import org.junit.jupiter.api.Test;

import static com.ralfhenze.rms.railnetworkplanning.domain.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

class RailNetworkDraftTest {

    @Test
    void should_ensure_unique_station_names() {
        final RailNetworkDraft draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(potsdamHbfName, potsdamHbfPos);

        assertThrows(IllegalArgumentException.class, () -> {
            draft.withNewStation(berlinHbfName, stuttgartHbfPos);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            draft.withRenamedStation(potsdamHbfName, berlinHbfName);
        });
    }

    @Test
    void should_ensure_minimum_distance_between_stations() {
        final RailNetworkDraft draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(potsdamHbfName, potsdamHbfPos);

        assertThrows(IllegalArgumentException.class, () -> {
            draft.withNewStation(berlinOstName, berlinOstPos);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            draft.withMovedStation(potsdamHbfName, berlinOstPos);
        });
    }

    @Test
    void should_ensure_no_duplicate_connections() {
        final RailNetworkDraft draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(potsdamHbfName, potsdamHbfPos)
            .withConnection(berlinHbfName, potsdamHbfName)
            .withConnection(potsdamHbfName, berlinHbfName); // the same connection again

        assertEquals(1, draft.getConnections().size());
    }

    @Test
    void should_ensure_max_track_length() {
        final RailNetworkDraft draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(stuttgartHbfName, stuttgartHbfPos);

        assertThrows(IllegalArgumentException.class, () -> {
            draft.withConnection(berlinHbfName, stuttgartHbfName);
        });
    }

    @Test
    void should_be_able_to_delete_stations() {
        RailNetworkDraft draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos);
        assertEquals(1, draft.getStations().size());

        draft = draft.withoutStation(berlinHbfName);

        assertTrue(draft.getStations().isEmpty());
    }

    @Test
    void should_be_able_to_delete_connections() {
        RailNetworkDraft draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(potsdamHbfName, potsdamHbfPos)
            .withConnection(berlinHbfName, potsdamHbfName);

        assertEquals(1, draft.getConnections().size());

        draft = draft.withoutConnection(potsdamHbfName, berlinHbfName);

        assertTrue(draft.getConnections().isEmpty());
    }

    @Test
    void should_delete_associated_connections_on_station_delete() {
        RailNetworkDraft draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(potsdamHbfName, potsdamHbfPos)
            .withConnection(berlinHbfName, potsdamHbfName);

        assertEquals(2, draft.getStations().size());
        assertEquals(1, draft.getConnections().size());

        draft = draft.withoutStation(potsdamHbfName);

        assertEquals(1, draft.getStations().size());
        assertTrue(draft.getConnections().isEmpty());
    }
}
