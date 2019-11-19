package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
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
    void should_ensure_no_duplicate_tracks() {
        final RailNetworkDraft draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(potsdamHbfName, potsdamHbfPos)
            .withNewTrack(berlinHbfName, potsdamHbfName)
            .withNewTrack(potsdamHbfName, berlinHbfName);

        assertEquals(1, draft.getTracks().size());
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
    void should_be_able_to_delete_tracks() {
        RailNetworkDraft draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(potsdamHbfName, potsdamHbfPos)
            .withNewTrack(berlinHbfName, potsdamHbfName);

        assertEquals(1, draft.getTracks().size());

        draft = draft.withoutTrack(potsdamHbfName, berlinHbfName);

        assertTrue(draft.getTracks().isEmpty());
    }

    @Test
    void should_delete_associated_tracks_on_station_delete() {
        RailNetworkDraft draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(potsdamHbfName, potsdamHbfPos)
            .withNewTrack(berlinHbfName, potsdamHbfName);

        assertEquals(2, draft.getStations().size());
        assertEquals(1, draft.getTracks().size());

        draft = draft.withoutStation(potsdamHbfName);

        assertEquals(1, draft.getStations().size());
        assertTrue(draft.getTracks().isEmpty());
    }
}
