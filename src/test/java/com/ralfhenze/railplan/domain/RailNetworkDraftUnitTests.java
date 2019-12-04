package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import org.junit.jupiter.api.Test;

import static com.ralfhenze.railplan.domain.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RailNetworkDraftUnitTests {

    @Test
    void should_ensure_unique_station_names_when_renaming() {
        final var draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(potsdamHbfName, potsdamHbfPos);

        assertThatExceptionOfType(ValidationException.class).isThrownBy(() -> {
            draft.withRenamedStation(potsdamHbfName, berlinHbfName);
        });
    }

    @Test
    void should_ensure_minimum_distance_between_stations_when_relocating() {
        final var draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(potsdamHbfName, potsdamHbfPos);

        assertThatExceptionOfType(ValidationException.class).isThrownBy(() -> {
            draft.withMovedStation(potsdamHbfName, berlinOstPos);
        });
    }

    @Test
    void should_ensure_no_duplicate_tracks() {
        final var draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(potsdamHbfName, potsdamHbfPos)
            .withNewTrack(berlinHbfName, potsdamHbfName);

        assertThatExceptionOfType(ValidationException.class).isThrownBy(() -> {
            draft.withNewTrack(potsdamHbfName, berlinHbfName);
        });
    }

    @Test
    void should_be_able_to_delete_stations() {
        var draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos);
        assertThat(draft.getStations()).hasSize(1);

        draft = draft.withoutStation(berlinHbfName);

        assertThat(draft.getStations()).isEmpty();
    }

    @Test
    void should_be_able_to_delete_tracks() {
        var draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(potsdamHbfName, potsdamHbfPos)
            .withNewTrack(berlinHbfName, potsdamHbfName);

        assertThat(draft.getTracks()).hasSize(1);

        draft = draft.withoutTrack(potsdamHbfName, berlinHbfName);

        assertThat(draft.getTracks()).isEmpty();
    }

    @Test
    void should_delete_associated_tracks_on_station_delete() {
        var draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(potsdamHbfName, potsdamHbfPos)
            .withNewTrack(berlinHbfName, potsdamHbfName);

        assertThat(draft.getStations()).hasSize(2);
        assertThat(draft.getTracks()).hasSize(1);

        draft = draft.withoutStation(potsdamHbfName);

        assertThat(draft.getStations()).hasSize(1);
        assertThat(draft.getTracks()).isEmpty();
    }
}
