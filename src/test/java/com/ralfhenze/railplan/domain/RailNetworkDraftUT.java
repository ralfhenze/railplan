package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import org.junit.jupiter.api.Test;

import static com.ralfhenze.railplan.domain.TestData.berlinHbfName;
import static com.ralfhenze.railplan.domain.TestData.berlinHbfPos;
import static com.ralfhenze.railplan.domain.TestData.berlinOstPos;
import static com.ralfhenze.railplan.domain.TestData.potsdamHbfName;
import static com.ralfhenze.railplan.domain.TestData.potsdamHbfPos;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RailNetworkDraftUT {

    @Test
    void ensuresUniqueStationNamesWhenRenaming() {
        final var draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(potsdamHbfName, potsdamHbfPos);

        assertThatExceptionOfType(ValidationException.class).isThrownBy(() -> {
            draft.withRenamedStation(potsdamHbfName, berlinHbfName);
        });
    }

    @Test
    void ensuresMinimumStationDistanceWhenRelocating() {
        final var draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(potsdamHbfName, potsdamHbfPos);

        assertThatExceptionOfType(ValidationException.class).isThrownBy(() -> {
            draft.withMovedStation(potsdamHbfName, berlinOstPos);
        });
    }

    @Test
    void ensuresNoDuplicateTracks() {
        final var draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(potsdamHbfName, potsdamHbfPos)
            .withNewTrack(berlinHbfName, potsdamHbfName);

        assertThatExceptionOfType(ValidationException.class).isThrownBy(() -> {
            draft.withNewTrack(potsdamHbfName, berlinHbfName);
        });
    }

    @Test
    void stationsCanBeDeleted() {
        var draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos);
        assertThat(draft.getStations()).hasSize(1);

        draft = draft.withoutStation(berlinHbfName);

        assertThat(draft.getStations()).isEmpty();
    }

    @Test
    void tracksCanBeDeleted() {
        var draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(potsdamHbfName, potsdamHbfPos)
            .withNewTrack(berlinHbfName, potsdamHbfName);

        assertThat(draft.getTracks()).hasSize(1);

        draft = draft.withoutTrack(potsdamHbfName, berlinHbfName);

        assertThat(draft.getTracks()).isEmpty();
    }

    @Test
    void deletesAssociatedTracksWhenStationIsDeleted() {
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
