package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import org.junit.Test;

import static com.ralfhenze.railplan.domain.TestData.berlinHbfName;
import static com.ralfhenze.railplan.domain.TestData.berlinHbfPos;
import static com.ralfhenze.railplan.domain.TestData.berlinOstPos;
import static com.ralfhenze.railplan.domain.TestData.frankfurtHbfName;
import static com.ralfhenze.railplan.domain.TestData.frankfurtHbfPos;
import static com.ralfhenze.railplan.domain.TestData.hamburgHbfName;
import static com.ralfhenze.railplan.domain.TestData.hamburgHbfPos;
import static com.ralfhenze.railplan.domain.TestData.potsdamHbfName;
import static com.ralfhenze.railplan.domain.TestData.potsdamHbfPos;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class RailNetworkDraftUT {

    @Test
    public void keepsStationOrderWhenUpdatingStation() {
        final var draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(potsdamHbfName, potsdamHbfPos)
            .withNewStation(frankfurtHbfName, frankfurtHbfPos);

        final var updatedDraft = draft
            .withUpdatedStation(potsdamHbfName, hamburgHbfName, hamburgHbfPos);

        final var stationNames = updatedDraft.getStations().collect(TrainStation::getName);
        assertThat(stationNames).containsExactly(berlinHbfName, hamburgHbfName, frankfurtHbfName);
    }

    @Test
    public void ensuresUniqueStationNamesWhenRenaming() {
        var draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(potsdamHbfName, potsdamHbfPos);

        draft = draft.withUpdatedStation(potsdamHbfName, berlinHbfName, potsdamHbfPos);

        assertThat(draft.isValid()).isFalse();
    }

    @Test
    public void ensuresMinimumStationDistanceWhenRelocating() {
        var draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(potsdamHbfName, potsdamHbfPos);

        draft = draft.withUpdatedStation(potsdamHbfName, potsdamHbfName, berlinOstPos);

        assertThat(draft.isValid()).isFalse();
    }

    @Test
    public void ensuresNoDuplicateTracks() {
        var draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(potsdamHbfName, potsdamHbfPos)
            .withNewTrack(berlinHbfName, potsdamHbfName);

        draft = draft.withNewTrack(potsdamHbfName, berlinHbfName);

        assertThat(draft.isValid()).isFalse();
    }

    @Test
    public void deletesAssociatedTracksWhenStationIsDeleted() {
        var draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(potsdamHbfName, potsdamHbfPos)
            .withNewTrack(berlinHbfName, potsdamHbfName);

        draft = draft.withoutStation(potsdamHbfName);

        assertThat(draft.getStations()).hasSize(1);
        assertThat(draft.getTracks()).isEmpty();
    }

    @Test
    public void providesPossibilityToDeleteTracks() {
        var draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(hamburgHbfName, hamburgHbfPos)
            .withNewTrack(berlinHbfName, hamburgHbfName);

        draft = draft.withoutTrack(berlinHbfName, hamburgHbfName);

        assertThat(draft.getTracks()).isEmpty();
    }

    @Test
    public void throwsExceptionWhenDeletingNonExistentTrack() {
        final var draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(hamburgHbfName, hamburgHbfPos)
            .withNewStation(potsdamHbfName, potsdamHbfPos)
            .withNewTrack(berlinHbfName, potsdamHbfName);

        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
            draft.withoutTrack(berlinHbfName, hamburgHbfName)
        );
    }
}
