package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import org.junit.jupiter.api.Test;

import static com.ralfhenze.railplan.domain.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

class RailNetworkDraftTest {

    @Test
    void should_ensure_unique_station_names_when_renaming() {
        final RailNetworkDraft draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(potsdamHbfName, potsdamHbfPos);

        assertThrows(IllegalArgumentException.class, () -> {
            draft.withRenamedStation(potsdamHbfName, berlinHbfName);
        });
    }

    @Test
    void should_ensure_minimum_distance_between_stations_when_relocating() {
        final RailNetworkDraft draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(potsdamHbfName, potsdamHbfPos);

        assertThrows(IllegalArgumentException.class, () -> {
            draft.withMovedStation(potsdamHbfName, berlinOstPos);
        });
    }

    @Test
    void should_ensure_no_duplicate_tracks() {
        final RailNetworkDraft draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(potsdamHbfName, potsdamHbfPos)
            .withNewTrack(berlinHbfName, potsdamHbfName);

        assertThrows(IllegalArgumentException.class, () -> {
            draft.withNewTrack(potsdamHbfName, berlinHbfName);
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
